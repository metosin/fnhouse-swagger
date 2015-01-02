(ns fnhouse.swagger-test
  (:require [fnhouse.swagger :refer :all]
            [midje.sweet :refer :all]))

(fact "swagger-ui (requires swagger-ui dependency)"
  (let [{:keys [status body]} ((wrap-swagger-ui identity)
                                {:uri "/index.html"})]
    status => 200
    (slurp body) => (contains "swagger")))
