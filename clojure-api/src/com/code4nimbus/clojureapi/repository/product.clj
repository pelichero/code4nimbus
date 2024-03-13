(ns com.code4nimbus.clojureapi.repository.product
  (:require [datomic.api :as d]
            [schema.core :as s]
            [com.code4nimbus.clojureapi.model.product :as model.product]))

(s/defn add! :- model.product/Product
  [product :- model.product/Product
   conn]
  (d/transact conn [product]))

(s/defn get-all :- [model.product/Product]
  [conn]
  (d/q '[:find (pull ?product [*])
         :where [?product :product/name]] (d/db conn)))

(s/defn by-name :- (s/maybe [model.product/Product])
  [conn
   name]
  (d/q '[:find (pull ?product [*])
         :in $ ?name
         :where [?product :product/name ?name]] (d/db conn) name))