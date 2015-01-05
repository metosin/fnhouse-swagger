(ns ^:no-doc fnhouse.swagger2
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

(defn- collect-route [ns-sym->prefix extra-metadata-fn api-routes annotated-handler]
  (letk [[[:info method path description request responses annotations
           [:source-map ns]]] annotated-handler]
    (let [ns-sym (symbol ns)
          prefix (ns-sym->prefix ns-sym)
          extra-metadata (or (extra-metadata-fn annotations) {})]
      (if (ignore-ns? ns-sym)
        api-routes
        (assoc-in api-routes [path method]
                  (merge extra-metadata
                         {:tags        [prefix]
                          :summary     description
                          :description description
                          :responses   (convert-responses responses)
                          :parameters  (convert-parameters request)}))))))

;;
;; Public API
;;

(defn collect-routes
  "Parameters:
  - seq of fnhouse AnnotatedProtoHandlers
  - prefix->ns-sym map
  - top-level base swagger extra parameters, defaults to {}
  - extra function (like fnhouse extra-info-fn). It takes contents of :annotations
  field on handler and returns a map that will be merged into
  ring-swagger's Operation-data. Such function can be used to obtain Swagger auth
  spec from fnhouse's handler or to override any fnhouse-swagger-derived
  metadata."
  ([handlers prefix->ns-sym]
    (collect-routes handlers prefix->ns-sym {}))
  ([handlers prefix->ns-sym base]
    (collect-routes handlers prefix->ns-sym base (constantly {})))
  ([handlers prefix->ns-sym base extra-metadata-fn]
    (let [ns-sym->prefix (map-invert prefix->ns-sym)
          reducer (partial collect-route ns-sym->prefix extra-metadata-fn)
          api-routes (reduce reducer {} handlers)]
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
