(ns com.code4nimbus.clojureapi.logic.misc
  (:require [schema.core :as s]))

;TODO: This should be a config file
(def env-bootstrap-server (str (System/getenv "BOOTSTRAP_SERVER")))

(s/defn bootstrap-server :- s/Str
  []
  env-bootstrap-server)

(s/defn product-topic :- s/Str
  []
  "product")