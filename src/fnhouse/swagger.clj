(ns ^:no-doc fnhouse.swagger
  (:require
    [plumbing.core :refer :all]
    [ring.swagger.core :as ring-swagger]
    [ring.swagger.ui :as ring-swagger-ui]
    [clojure.set :refer [map-invert]]
    [schema.core :as s]))

;;
;; Internals
;;

(defn- generate-nickname [annotated-handler]
  (get-in annotated-handler [:info :source-map :name]))

(defn- convert-parameters [request]
  (for [[type f] {:body :body, :query :query-params, :path :uri-args}
        :let [model (f request)]
        :when model]
    {:type type :model model}))

(defn- convert-response-messages [responses]
  (for [[code model] responses
         :when (not= code 200)
         :let [message (or (some-> model meta :message) "")]]
    {:code code, :message message, :responseModel model}))

(defn- ignore-ns? [ns-sym]
  (:no-doc (meta (the-ns ns-sym))))

(defn collect-route [ns-sym->prefix extra-metadata-fn routes annotated-handler]
  (letk [[[:info method path description request responses annotations
           [:source-map ns]]] annotated-handler]
    (let [ns-sym (symbol ns)
          prefix (ns-sym->prefix ns-sym)
          extra-metadata (or (extra-metadata-fn annotations) {})]
      (if (ignore-ns? ns-sym)
        routes
        (update-in routes [prefix :routes]
                   conj {:method method
                         :uri path
                         :metadata (merge
                                     {:summary description
                                      :return (get responses 200)
                                      :nickname (generate-nickname annotated-handler)
                                      :responseMessages (convert-response-messages responses)
                                      :parameters (convert-parameters request)}
                                     extra-metadata)})))))

(defn- collect-resource-meta [api-routes [ns-sym prefix]]
  (letk [[{doc nil}] (meta (the-ns ns-sym))]
    (if (ignore-ns? ns-sym)
      api-routes
      (update-in api-routes [prefix]
                 assoc :description doc))))

;;
;; Public API
;;

(defn collect-routes
  "The third parameter of this function, extra-metadata-fn, is supposed to be a
  counterpart of fnhouse's extra-info-fn. It takes contents of :annotations
  field on handler and returns a map that will be merged into
  ring-swagger's :metadata. Such function can be used to obtain Swagger auth
  spec from fnhouse's handler or to override any fnhouse-swagger-derived
  metadata"
  ([handlers prefix->ns-sym]
    (collect-routes handlers prefix->ns-sym (constantly {})))
  ([handlers prefix->ns-sym extra-metadata-fn]
    (let [ns-sym->prefix (map-invert prefix->ns-sym)
          route-collector (partial collect-route ns-sym->prefix extra-metadata-fn)
          api-routes (reduce route-collector {} handlers)]
      (reduce collect-resource-meta api-routes ns-sym->prefix))))

(def wrap-swagger-ui ring-swagger-ui/wrap-swagger-ui)

;;
;; Swagger 1.2 Endpoints
;;

(defnk $api-docs$GET
  "Apidocs"
  {:responses {200 s/Any}}
  [[:resources swagger {swagger-parameters {}}]]
  (ring-swagger/api-listing swagger-parameters swagger))

(defnk $api-docs$:**$GET
  "Apidoc"
  {:responses {200 s/Any}}
  [[:request uri-args :as request]
   [:resources swagger {swagger-parameters {}}]]
  (let [resource (safe-get uri-args :**)]
    (ring-swagger/api-declaration swagger-parameters swagger resource
      (ring-swagger/basepath request))))
