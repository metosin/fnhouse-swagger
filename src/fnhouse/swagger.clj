(ns ^:no-doc fnhouse.swagger
  (:require
    [plumbing.core :refer :all]
    [ring.swagger.spec2 :as swagger]
    [ring.swagger.ui :as swagger-ui]
    [clojure.set :refer [map-invert]]
    [schema.core :as s]))

;;
;; Internals
;;

(defn- convert-parameters [request]
  (for-map [[type f] {:body :body, :query :query-params, :path :uri-args}
            :let [model (f request)]
            :when (and model (not (empty? model)))]
    type model))

(defn- convert-responses [responses]
  (for-map [[code model] responses
            :let [message (or (some-> model meta :message) "")]]
    code {:description message, :schema model}))

(defn- ignore-ns? [ns-sym]
  (:no-doc (meta (the-ns ns-sym))))

(defn- collect-route [ns-sym->prefix api-routes annotated-handler]
  (letk [[[:info method path description request responses
           [:source-map ns]]] annotated-handler]
    (let [ns-sym (symbol ns)
          prefix (ns-sym->prefix ns-sym)]
      (if (ignore-ns? ns-sym)
        api-routes
        (assoc-in api-routes [path method]
                  {:tags [prefix]
                   :summary description
                   :description description
                   :responses (convert-responses responses)
                   :parameters (convert-parameters request)})))))

;;
;; Public API
;;

(defn collect-routes
  ([handlers prefix->ns-sym] (collect-routes handlers prefix->ns-sym {}))
  ([handlers prefix->ns-sym base]
    (let [ns-sym->prefix (map-invert prefix->ns-sym)
          api-routes (reduce (partial collect-route ns-sym->prefix) {} handlers)]
      (assoc base :paths api-routes))))

(def wrap-swagger-ui swagger-ui/wrap-swagger-ui)

;;
;; Swagger 2.0 Endpoint
;;

(defnk $swagger.json$GET
  "Swagger 2.0 Specs"
  {:responses {200 s/Any}}
  [[:resources swagger]]
  {:body (swagger/swagger-json swagger)})
