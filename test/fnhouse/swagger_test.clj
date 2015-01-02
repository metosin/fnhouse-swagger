(ns fnhouse.swagger-test
  (:require [fnhouse.swagger :refer :all]
            [midje.sweet :refer :all]

            [plumbing.core :refer [defnk fn->]]
            [fnhouse.handlers :as handlers]
            [fnhouse.routes :as routes]
            [fnhouse.middleware :as middleware]
            [schema.core :as s]))

;;
;; Test App
;;

(s/defschema Makkara {:id  Long, :name s/Str, :size (s/enum :S :M :L)})
(s/defschema NewMakkara (dissoc Makkara :id))

(defnk $:makkara-id$GET
  "Adds a Makkara"
  {:responses {200 Makkara}}
  [[:request [:uri-args makkara-id :- Long]]]
  {:body {:id 1 :name "Musta" :size :L}})

(defnk $POST
  "Adds a Makkara"
  {:responses {200 Makkara}}
  [[:request body :- NewMakkara]]
  {:body (assoc Makkara :id 1)})

(let [prefix->ns-sym {"makkarat" 'fnhouse.swagger-test
                      "api" 'fnhouse.swagger}
      proto-handlers (handlers/nss->proto-handlers prefix->ns-sym)
      swagger (collect-routes proto-handlers prefix->ns-sym)
      _ (./pprint proto-handlers)
      app (-> {:swagger swagger}
              ((handlers/curry-resources proto-handlers))
              #_(map (fn-> middleware/coercion-middleware (constantly nil) (constantly nil)))
              routes/root-handler)]
  app)

;;
;; Public api
;;

(fact "collect-routes"
  (let [prefix->ns-sym {"makkarat" 'fnhouse.swagger-test}
        proto-handlers (handlers/nss->proto-handlers prefix->ns-sym)
        swagger (collect-routes proto-handlers prefix->ns-sym)]
    swagger => {}
  ))

(fact "swagger-ui (requires swagger-ui dependency)"
  (let [{:keys [status body]} ((wrap-swagger-ui identity)
                                {:uri "/index.html"})]
    status => 200
    (slurp body) => (contains "swagger")))
