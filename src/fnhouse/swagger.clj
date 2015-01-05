(ns ^:no-doc fnhouse.swagger
  (:require
    [plumbing.core :refer :all]
    [ring.swagger.core2 :as ring-swagger2]
    [ring.swagger.ui :as ring-swagger-ui]
    [clojure.set :refer [map-invert]]
    [schema.core :as s]))

;;
;; Internals
;;

(defn- operation-id [annotated-handler]
  (get-in annotated-handler [:info :source-map :name]))

(defn- convert-parameters [request]
  (for-map [[type f] {:body :body, :query :query-params, :path :uri-args}
            :let [model (f request)]
            :when (and model (not (empty? model)))]
    type model))

(defn- convert-responses [responses]
  (for [[code model] responses
         :let [message (or (some-> model meta :message) "")]]
    {:code code, :description message, :schema model}))

(defn- dont-collect? [ns]
  (:no-doc (meta (the-ns ns))))

(defn- collect-route [ns-sym->prefix api-routes annotated-handler]
  (letk [[[:info method path description request responses
           [:source-map ns]]] annotated-handler]
    (let [prefix (ns-sym->prefix (symbol ns))]
      (if (dont-collect? (symbol ns))
        api-routes
        (update-in api-routes [path]
                   conj {:method method
                         :tags [prefix]
                         :summary description
                         :description description
                         :operationId (operation-id annotated-handler)
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

(def wrap-swagger-ui ring-swagger-ui/wrap-swagger-ui)

;;
;; Swagger 2.0 Endpoint
;;

(defnk $swagger.json$GET
  "Swagger 2.0 Specs"
  {:responses {200 s/Any}}
  [[:resources swagger]]
  (ring-swagger2/swagger-json swagger))
