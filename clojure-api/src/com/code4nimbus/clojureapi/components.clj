(ns com.code4nimbus.clojureapi.components
  (:require [com.code4nimbus.clojureapi.components.app :as components.app]
            [com.code4nimbus.clojureapi.components.database :as components.database]
            [com.stuartsierra.component :as component]))

(defn system []
  (-> (component/system-map
        :db (components.database/new-database)
        :app (component/using
               (components.app/map->WebServer {})
               [:db]))))