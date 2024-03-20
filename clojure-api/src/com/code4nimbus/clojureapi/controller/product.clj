(ns com.code4nimbus.clojureapi.controller.product
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [com.code4nimbus.clojureapi.domain.product :as domain.product]
            [com.code4nimbus.clojureapi.repository.product :as repository.product]
            [schema.core :as s]))

(s/defn add!
  [product :- domain.product/NewProduct
   conn]
  (-> (adapters.product/domain->model product)
      (repository.product/add! conn)
      (adapters.product/model->domain)))

(s/defn update! :- domain.product/NewProduct
  [product :- domain.product/Product
   conn]
  (-> (adapters.product/update-domain->model product)
      (repository.product/update! conn)
      (adapters.product/model->domain)))

(s/defn ^:private entities->domain
  [entities]
  (map (fn [product]
         (adapters.product/model->domain (into {} product)))
       entities))

(s/defn get-all :- [domain.product/NewProduct]
  [conn]
  (let [products (-> (repository.product/get-all conn)
                     (entities->domain))]
    (doall products)))

(s/defn by-name :- (s/maybe [domain.product/NewProduct])
  [conn
   name :- s/Str]
  (let [products (-> (repository.product/by-name conn name)
                     (entities->domain))]
    (doall products)))

(s/defn by-id :- (s/maybe domain.product/NewProduct)
  [conn
   id :- s/Int]
  (let [product (repository.product/by-id conn id)]
    (log/info product)
    (when (not (nil? (:product/name product)))
      (adapters.product/model->domain product))))

(s/defn delete!
  [conn
   id :- s/Int]
  (repository.product/delete! conn id))
