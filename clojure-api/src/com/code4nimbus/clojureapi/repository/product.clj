(ns com.code4nimbus.clojureapi.repository.product
  (:require [datomic.api :as d]
            [schema.core :as s]
            [com.code4nimbus.clojureapi.model.product :as model.product]))

(s/defn ^:private ->transaction :- model.product/Product
  [product :- model.product/Product]
  (assoc product
    :db/id (d/tempid :db.part/user)))

(s/defn add! :- model.product/Product
  [product :- model.product/Product
   conn]
  (let [product (->transaction product)
        temp-id (:db/id product)
        post-tx @(d/transact conn [product])
        db (:db-after post-tx)
        entity-id (d/resolve-tempid db (:tempids post-tx) temp-id)]
    (d/pull (d/db conn) '[*] entity-id)))

(s/defn update! :- model.product/Product
  [product :- model.product/Product
   conn]
  (let [post-tx @(d/transact conn [product])
        db (:db-after post-tx)]
    (d/pull db '[*] (:db/id product))))

(s/defn get-all :- [model.product/Product]
  [conn]
  (d/q '[:find (pull ?product [*])
         :where [?product :product/name]] (d/db conn)))

(s/defn by-id :- (s/maybe model.product/Product)
  [conn
   id :- Long]
  (d/pull (d/db conn) '[*] id))

(s/defn by-name :- (s/maybe [model.product/Product])
  [conn
   name]
  (d/q '[:find (pull ?product [*])
         :in $ ?name
         :where [?product :product/name ?name]] (d/db conn) name))

(s/defn delete!
  [conn
   id :- Long]
  (d/transact conn [[:db.fn/retractEntity id]]))