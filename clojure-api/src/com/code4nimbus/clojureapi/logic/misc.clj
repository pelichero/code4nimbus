(ns com.code4nimbus.clojureapi.logic.misc
  (:require [schema.core :as s]))

(s/defn bootstrap-server :- s/Str
  []
  "localhost:9092")

(s/defn product-topic :- s/Str
  []
  "product")