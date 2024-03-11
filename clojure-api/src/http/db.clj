(ns http.db
  (:require [datomic.api :as d]))

(def product-schema [{:db/ident       :product/name
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Product name"}
                     {:db/ident       :product/slug
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Path to access this product via HTTP"}
                     {:db/ident       :product/price
                      :db/valueType   :db.type/long
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Product price"}])

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
    (d/transact (d/connect uri) product-schema)
    (println "Schema created")
    (catch Exception error
      (println (str "Error creating schema, ex: " error)))))
