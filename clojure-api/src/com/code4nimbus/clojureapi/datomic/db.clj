(ns com.code4nimbus.clojureapi.datomic.db
  (:require [clojure.tools.logging :as log]
            [datomic.api :as d]
            [com.code4nimbus.clojureapi.model.product :as model.product]))

(def uri (if (nil? (System/getenv "DATOMIC_URL"))
           "datomic:mem://product"
           (str (System/getenv "DATOMIC_URL"))))

(defn get-conn
  []
  (log/info (str "Connecting to database: " uri))
  (d/connect uri))

(defn create-database
  []
  (d/create-database uri)
  (log/info (str "Database created " uri)))

(defn drop-database
  []
  (try
    (d/delete-database uri)
    (log/info (str "Database dropped " uri))
    (catch Exception ex
      (log/error "Database does not exist; ex: " ex))))

(defn create-schema
  []
  (try
    (d/transact (d/connect uri) model.product/product-schema)
    (log/info (str "Schema created " uri))
    (catch Exception error
      (log/error "Error creating schema, ex: " error))))

(defn configure
  []
  (drop-database)
  (create-database)
  (create-schema)
  (get-conn))
