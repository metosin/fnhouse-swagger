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

;;
;; Public api
;;

(fact "collect-routes"
  (let [prefix->ns-sym {"makkarat" 'fnhouse.swagger-test}
        proto-handlers (handlers/nss->proto-handlers prefix->ns-sym)
        swagger (collect-routes proto-handlers prefix->ns-sym)]
    swagger

    => {"makkarat"
        {:description nil
         :routes [{:method :post
                   :uri "/makkarat/"
                   :metadata {:summary "Adds a Makkara"
                              :return {:id Long
                                       :name String
                                       :size (s/enum :L :M :S)}
                              :nickname "makkarat$POST"
                              :responseMessages []
                              :parameters [{:type :path, :model {}}
                                           {:type :query
                                            :model {s/Keyword String}}
                                           {:type :body
                                            :model {:name String
                                                    :size (s/enum :L :M :S)}}]}}
                  {:method :get
                   :uri "/makkarat/:makkara-id"
                   :metadata {:summary "Adds a Makkara"
                              :return {:id Long
                                       :name String
                                       :size (s/enum :L :M :S)}
                              :nickname "makkarat$:makkara-id$GET"
                              :responseMessages []
                              :parameters [{:type :path, :model {:makkara-id Long}}
                                           {:type :query :model {s/Keyword String}}
                                           {:type :body, :model nil}]}}]}}))

(fact "swagger-ui (requires swagger-ui dependency)"
  (let [{:keys [status body]} ((wrap-swagger-ui identity)
                                {:uri "/index.html"})]
    status => 200
    (slurp body) => (contains "swagger")))
