(ns com.code4nimbus.clojureapi.components
  (:require [com.code4nimbus.clojureapi.components.app :as components.app]
            [com.code4nimbus.clojureapi.components.database :as components.database]
            [com.code4nimbus.clojureapi.components.message :as components.message]
            [com.code4nimbus.clojureapi.diplomat.consumer :refer [product-handler]]
            [com.stuartsierra.component :as component]))

(def datomic-uri (or (System/getenv "DATOMIC_URL")
                     "datomic:mem://product"))

(defn system []
  (-> (component/system-map
        :db (components.database/map->Database {:uri datomic-uri})
        :product-consumer (component/using
                            (components.message/map->Kafka {:topic            "product"
                                                            :bootstrap-server (or (System/getenv "BOOTSTRAP_SERVER")
                                                                                  "localhost:9092")
                                                            :handler          product-handler})
                            [:db])
        :app (component/using
               (components.app/map->WebServer {})
               [:db]))))

(defn system-test []
  (-> (component/system-map
        :db (components.database/map->Database {:uri datomic-uri})
        :app (component/using
               (components.app/map->WebServer {})
               [:db]))))