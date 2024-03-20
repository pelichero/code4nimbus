(ns com.code4nimbus.clojureapi.diplomat.consumer
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.logic.misc :as logic.misc]
            [environ.core :refer [env]])
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

(defn consumer-subscribe
  [consumer topic]
  (.subscribe consumer [topic]))

(defn product-listener
  []
  (let [product-topic (logic.misc/product-topic)
        bootstrap-server (env :bootstrap-server (logic.misc/bootstrap-server))
        consumer (build-consumer bootstrap-server)]
    (consumer-subscribe consumer product-topic)
    (while true
      (let [records (.poll consumer (Duration/ofMillis 100))]
        (doseq [record records]
          (log/info (str "Processed Value: " (.value record)))))
      (.commitAsync consumer))))