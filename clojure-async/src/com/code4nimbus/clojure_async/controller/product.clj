(ns com.code4nimbus.clojure-async.controller.product
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojure-async.diplomat.producer :as diplomat.producer]
            [com.code4nimbus.clojure-async.domain.product :as domain.product]
            [schema.core :as s]))

(s/defn send!
  [product :- domain.product/NewProduct]
  (try
    (diplomat.producer/send-product! product)
    (catch Exception e
      (log/error "Error sending message %s" (.getMessage e))
      (throw e))))
