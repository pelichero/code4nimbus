(ns com.code4nimbus.clojureapi.diplomat.consumer
  (:require [clojure.tools.logging :as log]
            [environ.core :refer [env]]
            [com.code4nimbus.clojureapi.logic.misc :as logic.misc])
  (:import (java.time Duration)
           (org.apache.kafka.clients.admin AdminClientConfig KafkaAdminClient NewTopic)
           org.apache.kafka.clients.consumer.KafkaConsumer
           (org.apache.kafka.common TopicPartition)
           (org.apache.kafka.common.serialization StringDeserializer)))

(defn create-topics!
  "Create the topic "
  [bootstrap-server topics ^Integer partitions ^Short replication]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (KafkaAdminClient/create config)
        new-topics (map (fn [^String topic-name] (NewTopic. topic-name partitions replication)) topics)]
    (.createTopics adminClient new-topics)))

(defn- pending-messages
  [end-offsets consumer]
  (some true? (doall
                (map
                  (fn [topic-partition]
                    (let [position (.position consumer (key topic-partition))
                          value (val topic-partition)]
                      (< position value)))
                  end-offsets))))

(defn search-topic-by-key
  [^KafkaConsumer consumer topic search-key]
  (let [topic-partitions (->> (.partitionsFor consumer topic)
                              (map #(TopicPartition. (.topic %) (.partition %))))
        _ (.assign consumer topic-partitions)
        _ (.seekToBeginning consumer (.assignment consumer))
        end-offsets (.endOffsets consumer (.assignment consumer))
        found-records (transient [])]
    (log/info "end offsets %s" end-offsets)
    (log/info "Pending messages? %s" (pending-messages end-offsets consumer))
    (while (pending-messages end-offsets consumer)
      (log/info "Pending messages? %s" (pending-messages end-offsets consumer))
      (let [records (.poll consumer (Duration/ofMillis 50))
            matched-search-key (filter #(= (.key %) search-key) records)]
        (conj! found-records matched-search-key)
        (doseq [record matched-search-key]
          (log/info "Found Matching Key %s Value %s" (.key record) (.value record)))))
    (persistent! found-records)))

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

(defn start-kafka-listeners
  []
  (let [product-topic (logic.misc/product-topic)
        bootstrap-server (env :bootstrap-server (logic.misc/bootstrap-server))
        replay-consumer (build-consumer bootstrap-server)
        consumer (build-consumer bootstrap-server)]
    (create-topics! bootstrap-server [product-topic] 1 1)
    (search-topic-by-key replay-consumer product-topic "1")
    (consumer-subscribe consumer product-topic)
    (while true
      (let [records (.poll consumer (Duration/ofMillis 100))]
        (doseq [record records]
          (log/info (str "Processed Value: " (.value record)))))
      (.commitAsync consumer))))