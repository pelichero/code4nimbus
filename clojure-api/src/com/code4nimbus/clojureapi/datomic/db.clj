(ns com.code4nimbus.clojureapi.datomic.db
  (:require [datomic.api :as d]
            [com.code4nimbus.clojureapi.model.product :as model.product]))

(def uri "datomic:dev://datomic-transactor:4334/product/?password=unsafe")

(defn get-conn
  []
  (d/connect uri))

(defn create-database
  []
  (d/create-database uri)
  (println "Database created"))

(defn drop-database
  []
  (try
    (d/delete-database uri)
    (println "Database dropped")
    (catch Exception _
      (println "Database does not exist"))))

(defn create-schema
  []
  (try
    (d/transact (d/connect uri) model.product/product-schema)
    (println "Schema created")
    (catch Exception error
      (println (str "Error creating schema, ex: " error)))))
