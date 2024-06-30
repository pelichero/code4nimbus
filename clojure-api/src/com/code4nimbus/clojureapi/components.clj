(ns com.code4nimbus.clojureapi.components
  (:require [com.code4nimbus.clojureapi.components.app :as components.app]
            [com.code4nimbus.clojureapi.components.database :as components.database]
            [com.code4nimbus.clojureapi.components.message :as components.message]
            [com.code4nimbus.clojureapi.diplomat.consumer :refer [product-handler]]
            [com.stuartsierra.component :as component]))

(defn system []
  (-> (component/system-map
        :db (components.database/new-database)
        :product-consumer (component/using
                            (components.message/map->Kafka {:topic            "product"
                                                            :bootstrap-server (or (System/getenv "BOOTSTRAP_SERVER")
                                                                                  "localhost:9092")
                                                            :handler          product-handler})
                            [:db])
        :app (component/using
               (components.app/map->WebServer {})
               [:db]))))