(ns com.code4nimbus.clojureapi.controller.product
  (:require [schema.core :as s]
            [com.code4nimbus.clojureapi.model.product :as model.product]
            [com.code4nimbus.clojureapi.repository.product :as repository.product]))

(s/defn add!
  [product :- model.product/Product
   conn]
  (repository.product/add! product conn))

(s/defn get-all :- [model.product/Product]
  [conn]
  (repository.product/get-all conn))

(s/defn by-name :- (s/maybe [model.product/Product])
  [conn
   name]
  (print name)
  (repository.product/by-name conn name))