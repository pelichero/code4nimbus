(ns http.crud
  (:require [datomic.api :as d]
            [http.model :as model]))

(def uri "datomic:dev://datomic-transactor:4334/product/?password=unsafe")

(defn create-connection
  []
  (d/create-database uri)
  (d/connect uri))

(defn add-product
  [name slug price]
  (let [product (model/new-product name slug price)]
    (d/transact (d/connect uri) [product])))

(defn ^:private get-all-products
  [db]
  (d/q '[:find ?name
         :where [_ :product/name ?name]] db))

(defn get-products
  []
  (get-all-products (d/db (d/connect uri))))

(defn drop-database
  []
  (d/delete-database uri))