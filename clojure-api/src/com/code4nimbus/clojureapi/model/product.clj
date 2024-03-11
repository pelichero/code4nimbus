(ns com.code4nimbus.clojureapi.model.product
  (:require [schema.core :as s]))

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

(s/defschema Product
  #:product{:name  s/Str
            :slug  s/Str
            :price s/Num})