(ns com.code4nimbus.clojureapi.components.message
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component])
  (:import (java.time Duration)
           org.apache.kafka.clients.consumer.KafkaConsumer
           (org.apache.kafka.common.serialization StringDeserializer)))

(defn build-consumer
  [bootstrap-server]
  (let [consumer-props
        {"bootstrap.servers",  bootstrap-server
         "group.id",           "example"
         "key.deserializer",   StringDeserializer
         "value.deserializer", StringDeserializer
         "auto.offset.reset",  "earliest"
         "enable.auto.commit", "true"}]
    (KafkaConsumer. consumer-props)))

(defrecord Kafka [db topic bootstrap-server handler] component/Lifecycle
  (start [this]
    (log/info (str "Starting Kafka listeners on server " bootstrap-server " for topic: " topic))
    (let [consumer (build-consumer bootstrap-server)]
      (.subscribe consumer [topic])
      (while true
        (doseq [record (.poll consumer (Duration/ofMillis 100))]
          (handler record db))
        (.commitAsync consumer))
      (assoc this :consumer consumer)))
  (stop [this]
    (log/info "Stopping Kafka listeners...")
    this))