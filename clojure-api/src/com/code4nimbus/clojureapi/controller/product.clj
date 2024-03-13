(ns com.code4nimbus.clojureapi.controller.product
  (:require [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [com.code4nimbus.clojureapi.domain.product :as domain.product]
            [com.code4nimbus.clojureapi.repository.product :as repository.product]
            [schema.core :as s]))

(s/defn add!
  [product :- domain.product/Product
   conn]
  (-> (adapters.product/domain->model product)
      (repository.product/add! conn)
      (adapters.product/model->domain)))

(s/defn get-all :- [domain.product/Product]
  [conn]
  (map (fn [product]
         (adapters.product/model->domain product))
       (repository.product/get-all conn)))

(s/defn by-name :- (s/maybe [domain.product/Product])
  [conn
   name :- s/Str]
  (map (fn [product]
         (adapters.product/model->domain product))
       (repository.product/by-name conn name)))