(ns com.code4nimbus.clojureapi.components.database
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.model.product :as model.product]
            [datomic.api :as d]))

(defn ^:private create-database
  [uri]
  (d/create-database uri)
  (log/info (str "Database created " uri)))

(defn ^:private drop-database
  [uri]
  (try
    (d/delete-database uri)
    (log/info (str "Database dropped " uri))
    (catch Exception ex
      (log/error "Database does not exist; ex: " ex))))

(defn ^:private create-schema
  [uri]
  (try
    (d/transact (d/connect uri) model.product/product-schema)
    (log/info (str "Schema created " uri))
    (catch Exception error
      (log/error "Error creating schema, ex: " error))))

(defn ^:private configure
  [uri]
  (do (drop-database uri)
      (create-database uri)
      (create-schema uri)
      (d/connect uri)))

(defrecord Database [uri connection]
  component/Lifecycle
  (start [component]
    (let [conn (configure uri)]
      (assoc component :connection conn)))
  (stop [component]
    (log/info "Closing connection...")
    (.close connection)
    (assoc component :connection nil)))