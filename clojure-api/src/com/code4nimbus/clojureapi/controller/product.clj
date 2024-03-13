(ns com.code4nimbus.clojureapi.controller.product
  (:require [schema.core :as s]
            [com.code4nimbus.clojureapi.model.product :as model.product]
            [com.code4nimbus.clojureapi.repository.product :as repository.product]))

(s/defn add!
  [product :- model.product/Product
   conn]
  (repository.product/add! product conn))

(defn get-all
  [conn]
  (repository.product/get-all conn))

