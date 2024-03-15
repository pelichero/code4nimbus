(ns com.code4nimbus.clojureapi.datomic.db
  (:require [clojure.tools.logging :as log]
            [datomic.api :as d]
            [com.code4nimbus.clojureapi.model.product :as model.product]
            [clojure.tools.logging :as [clojure.tools.logging :as log]]))

(def uri (str (System/getenv "DATOMIC_URL")))

(defn get-conn
  []
  (d/connect uri))

(defn create-database
  []
  (d/create-database uri)
  (log/info "Database created"))

(defn drop-database
  []
  (try
    (d/delete-database uri)
    (log/info "Database dropped")
    (catch Exception ex
      (log/error "Database does not exist; ex: " ex))))

(defn create-schema
  []
  (try
    (d/transact (d/connect uri) model.product/product-schema)
    (log/info "Schema created")
    (catch Exception error
      (log/error "Error creating schema, ex: " error))))
