(ns com.code4nimbus.clojureapi.components.database
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.model.product :as model.product]
            [datomic.api :as d]))

(def uri (if (nil? (System/getenv "DATOMIC_URL"))
           "datomic:mem://product"
           (str (System/getenv "DATOMIC_URL"))))

(defn ^:private get-conn
  []
  (log/info (str "Connecting to database: " uri))
  (d/connect uri))

(defn ^:private create-database
  []
  (d/create-database uri)
  (log/info (str "Database created " uri)))

(defn ^:private drop-database
  []
  (try
    (d/delete-database uri)
    (log/info (str "Database dropped " uri))
    (catch Exception ex
      (log/error "Database does not exist; ex: " ex))))

(defn ^:private create-schema
  []
  (try
    (d/transact (d/connect uri) model.product/product-schema)
    (log/info (str "Schema created " uri))
    (catch Exception error
      (log/error "Error creating schema, ex: " error))))

(defn ^:private configure
  []
  (do (drop-database)
      (create-database)
      (create-schema)
      (get-conn)))

(defrecord Database [_host _port connection]
  component/Lifecycle

  (start [component]
    (let [conn (configure)]
      (assoc component :connection conn)))

  (stop [component]
    (log/info "Closing connection...")
    (.close connection)
    (assoc component :connection nil)))

(defn new-database []
  (map->Database {}))