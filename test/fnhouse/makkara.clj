(ns fnhouse.makkara
  (:require [plumbing.core :refer [defnk]]
            [schema.core :as s]))

(s/defschema Makkara {:id  Long, :name s/Str, :size (s/enum :S :M :L)})
(s/defschema NewMakkara (dissoc Makkara :id))

(defnk $:makkara-id$GET
  "Gets a Makkara"
  {:responses {200 Makkara}}
  [[:request [:uri-args makkara-id :- Long]]]
  {:body {:id 1 :name "Musta" :size :L}})

(defnk $POST
  "Adds a Makkara"
  {:responses {200 Makkara}}
  [[:request body :- NewMakkara]]
  {:body (assoc Makkara :id 1)})
