(ns com.code4nimbus.clojureapi.controller.product
  (:require [datomic.api :as d]
            [schema.core :as s]
            [com.code4nimbus.clojureapi.model.product :as model.product]))

(s/defn add!
  [product :- model.product/Product
   conn]
  (d/transact conn [product]))

(defn get-all
  [conn]
  (d/q '[:find ?name
         :where [_ :product/name ?name]] (d/db conn)))

