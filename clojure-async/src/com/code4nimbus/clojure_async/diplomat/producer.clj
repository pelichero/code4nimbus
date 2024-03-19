(ns com.code4nimbus.clojure-async.diplomat.producer
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojure-async.domain.product :as domain.product]
            [com.code4nimbus.clojure-async.logic.misc :as logic.misc]
            [clojure.data.json :as json]
            [schema.core :as s])
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer)))

(defn build-producer ^KafkaProducer
  [bootstrap-server]
  (let [producer-props {"value.serializer"  StringSerializer
                        "key.serializer"    StringSerializer
                        "bootstrap.servers" bootstrap-server}]
    (KafkaProducer. producer-props)))

(s/defn send-product!
  [product :- domain.product/NewProduct]
  (let [bootstrap-server (logic.misc/bootstrap-server)
        producer (build-producer bootstrap-server)
        json-product (json/write-str product)
        product-topic (logic.misc/product-topic)
        record (ProducerRecord. product-topic json-product)]
    (try
      (.send producer record)
      (catch Exception e
        (log/error "Error sending message %s" (.getMessage e))))))