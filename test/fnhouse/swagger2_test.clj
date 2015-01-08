(ns fnhouse.swagger2-test
  (:require [fnhouse.swagger2 :refer :all]
            [midje.sweet :refer :all]
            [fnhouse.makkara :as m]
            [plumbing.core :refer [defnk]]
            [fnhouse.handlers :as handlers]
            [ring.swagger.swagger2 :as swagger]
            [schema.core :as s]))

;;
;; Swagger 2.0
;;

(fact "collect-routes"
  (let [prefix->ns-sym {"makkarat" 'fnhouse.makkara
                        "" 'fnhouse.swagger2}
        proto-handlers (handlers/nss->proto-handlers prefix->ns-sym)
        swagger (collect-routes proto-handlers prefix->ns-sym {:info {:title   "Makkara API"
                                                                      :version "1.0"}})]

    (fact "produces valid Swagger-data"
      (s/check swagger/Swagger swagger) => nil)

    (fact "is mapped correctly"

      swagger =>

      {:info {:title "Makkara API"
              :version "1.0"}
       :paths {"/makkarat/" {:post {:parameters {:body m/NewMakkara
                                                 :query {s/Keyword s/Str}}
                                    :responses {200 {:description ""
                                                     :schema m/Makkara}}
                                    :summary "Adds a Makkara"
                                    :tags ["makkarat"]}}
               "/makkarat/:makkara-id" {:get {:parameters {:path {:makkara-id Long}
                                                           :query {s/Keyword s/Str}}
                                              :responses {200 {:description ""
                                                               :schema m/Makkara}}
                                              :summary "Gets a Makkara"
                                              :tags ["makkarat"]}}}}))

  (fact "swagger-ui (requires swagger-ui dependency)"
    (let [{:keys [status body]} ((wrap-swagger-ui identity)
                                  {:uri "/index.html"})]
      status => 200
      (slurp body) => (contains "swagger"))))
