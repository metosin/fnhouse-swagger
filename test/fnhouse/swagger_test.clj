(ns fnhouse.swagger-test
  (:require [fnhouse.swagger :refer :all]
            [midje.sweet :refer :all]
            [fnhouse.makkara :as m]
            [plumbing.core :refer [defnk]]
            [fnhouse.handlers :as handlers]
            [schema.core :as s]))

;;
;; Swagger 1.2
;;

(fact "collect-routes"
  (let [prefix->ns-sym {"makkarat" 'fnhouse.makkara
                        "api" 'fnhouse.swagger}
        proto-handlers (handlers/nss->proto-handlers prefix->ns-sym)
        swagger (collect-routes proto-handlers prefix->ns-sym)]
    swagger

    => {"makkarat"
        {:description nil
         :routes [{:method :post
                   :uri "/makkarat/"
                   :metadata {:summary "Adds a Makkara"
                              :return m/Makkara
                              :nickname "$POST"
                              :responseMessages []
                              :parameters [{:type :path
                                            :model {}}
                                           {:type :query
                                            :model {s/Keyword String}}
                                           {:type :body
                                            :model m/NewMakkara}]}}
                  {:method :get
                   :uri "/makkarat/:makkara-id"
                   :metadata {:summary "Gets a Makkara"
                              :return m/Makkara
                              :nickname "$:makkara-id$GET"
                              :responseMessages []
                              :parameters [{:type :path
                                            :model {:makkara-id Long}}
                                           {:type :query
                                            :model {s/Keyword String}}]}}]}}))

(fact "swagger-ui (requires swagger-ui dependency)"
  (let [{:keys [status body]} ((wrap-swagger-ui identity)
                                {:uri "/index.html"})]
    status => 200
    (slurp body) => (contains "swagger")))
