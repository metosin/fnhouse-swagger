(ns fnhouse.swagger
  "Swagger documentation"
  (:use plumbing.core)
  (:require
    [fnhouse.handlers :as handlers]
    [ring.swagger.core :as ring-swagger]
    [ring.swagger.ui :as ring-swagger-ui]
    [clojure.set :refer [map-invert]]
    [schema.core :as s]
    [ring.middleware.resource :as resource]))

(defn- generate-nickname [prefix annotated-handler]
  (str prefix (get-in annotated-handler [:info :source-map :name])))

(defn- convert-parameters [request]
  (for [[type f] {:body :body, :query :query-params, :path :uri-args}]
    {:type type :model (f request)}))

(defn- convert-response-messages [responses]
  (for [[code model] responses
         :when (not= code 200)
         :let [message (or (some-> model meta :message) "")]]
    {:code code, :message message, :responseModel model}))

(defn collect-route [ns-sym->prefix extra-metadata-fn api-routes annotated-handler]
  (letk [[[:info method path description request responses annotations
           [:source-map ns]]] annotated-handler]
    (let [prefix (ns-sym->prefix (symbol ns))
          extra-metadata (or (extra-metadata-fn annotations) {})]
      (update-in api-routes [prefix :routes]
        conj {:method method
              :uri path
              :metadata (merge
                         {:summary description
                          :return (get responses 200)
                          :nickname (generate-nickname prefix annotated-handler)
                          :responseMessages (convert-response-messages responses)
                          :parameters (convert-parameters request)}
                         extra-metadata)}))))

(defn collect-resource-meta [api-routes [ns-sym prefix]]
  (letk [[{doc nil}] (meta (the-ns ns-sym))]
    (update-in api-routes [prefix]
      assoc :description doc)))

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
         reducer (partial collect-route ns-sym->prefix extra-metadata-fn)
         api-routes (reduce reducer {} handlers)]
     (reduce collect-resource-meta api-routes ns-sym->prefix))))

(def swagger-ui ring-swagger-ui/wrap-swagger-ui)

(defnk $api-docs$GET
  "Apidocs"
  {:responses {200 s/Any}}
  [[:resources swagger swagger-parameters]]
  (ring-swagger/api-listing swagger-parameters swagger))

(defnk $api-docs$:**$GET
  "Apidoc"
  {:responses {200 s/Any}}
  [[:request uri-args :as request]
   [:resources swagger swagger-parameters]]
  (let [resource (safe-get uri-args :**)]
    (ring-swagger/api-declaration swagger-parameters swagger resource
      (ring-swagger/basepath request))))
