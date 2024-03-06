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
                      :db/valueType   :db.type/bigdec
                      :db/cardinality :db.cardinality/one
                      :db/doc         "Product price"}])

(defn create-schema [conn]
  (d/transact conn product-schema))