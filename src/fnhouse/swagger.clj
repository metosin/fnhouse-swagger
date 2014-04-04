(ns fnhouse.swagger
  (:use plumbing.core)
  (:require
    [fnhouse.handlers :as handlers]
    [ring.swagger.core :as ring-swagger]
    [clojure.set :refer [map-invert]]
    [schema.core :as s]
    [ring.middleware.resource :as resource]))

(defn- generate-nickname [annotated-handler]
  (str (:api annotated-handler) (get-in annotated-handler [:info :source-map :name])))

(defn- convert-parameters [request]
  (for [[type f] {:body :body, :query :query-params, :path :uri-args}]
    {:type type :model (f request)}))

(defn collect-route [ns-sym->prefix swagger annotated-handler]
  (let [{{:keys [method path description request responses source-map]} :info :as info} annotated-handler
        handler-ns (:ns source-map)
        prefix (ns-sym->prefix (symbol handler-ns))]
    (update-in swagger [prefix]
      update-in [:routes]
      conj {:method method
            :uri path
            :metadata {:summary description
                       :return (get responses 200)
                       :nickname (generate-nickname annotated-handler)
                       :parameters (convert-parameters request)}})))

(defn collect-routes [handlers prefix->ns-sym]
  (reduce (partial collect-route (map-invert prefix->ns-sym)) {} handlers))

(defn swagger-ui [handler]
  (resource/wrap-resource handler "swagger-ui"))

(defnk $api-docs$GET
  "Apidocs"
  {:responses {200 s/Any}}
  [[:resources swagger]]
  (ring-swagger/api-listing {} swagger))

(defnk $api-docs$:resource$GET
  "Apidoc"
  {:responses {200 s/Any}}
  [[:request [:uri-args resource :- String] :as request]
   [:resources swagger]]
  (ring-swagger/api-declaration {} swagger resource (ring-swagger/extract-basepath request)))
