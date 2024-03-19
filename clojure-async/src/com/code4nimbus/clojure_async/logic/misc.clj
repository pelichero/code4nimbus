(ns com.code4nimbus.clojure-async.logic.misc
  (:require [schema.core :as s]))

(def env-bootstrap-server (str (System/getenv "BOOTSTRAP_SERVER")))

(s/defn bootstrap-server :- s/Str
  []
  env-bootstrap-server)

(s/defn product-topic :- s/Str
  []
  "product")