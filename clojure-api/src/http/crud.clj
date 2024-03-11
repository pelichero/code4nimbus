(ns http.crud
  (:require [datomic.api :as d]
            [http.model :as model]))

(defn add-product
  [conn
   name
   slug
   price]
  (let [product (model/new-product name slug price)]
    (d/transact conn [product])))

(defn get-products
  [conn]
  (d/q '[:find ?name
         :where [_ :product/name ?name]] (d/db conn)))

