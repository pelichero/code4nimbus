(ns com.code4nimbus.clojureapi.adapters.product
  (:require [com.code4nimbus.clojureapi.model.product :as models.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojureapi.wire.out.product :as wire.out.product]
            [com.code4nimbus.clojureapi.domain.product :as domain.product]
            [clojure.tools.logging :as log]
            [schema.core :as s]))

(s/defn wire->domain :- wire.out.product/Product
  [{:keys [name slug price] :as wire} :- wire.in.product/Product]
  (log/info (str "update->domain" wire))
  {:name  name
   :slug  slug
   :price price})

(s/defn wire-update->domain :- wire.out.product/Product
  [{:keys [id name slug price] :as wire} :- wire.in.product/ProductUpdate]
  (log/info (str "wire-update->domain" wire))
  {:id    id
   :name  name
   :slug  slug
   :price price})

(s/defn domain->wire :- wire.out.product/Product
  [{:keys [id name slug price] :as domain} :- domain.product/Product]
  (log/info (str "domain->wire" domain))
  {:id    id
   :name  name
   :slug  slug
   :price price})

(s/defn update-domain->model :- models.product/Product
  [{:keys [id name slug price] :as domain} :- domain.product/Product]
  (log/info (str "domain->model" domain))
  {:db/id         id
   :product/name  name
   :product/slug  slug
   :product/price price})

(s/defn domain->model :- models.product/Product
  [{:keys [name slug price] :as domain} :- domain.product/NewProduct]
  (log/info (str "domain->model" domain))
  {:product/name  name
   :product/slug  slug
   :product/price price})

(s/defn model->domain :- domain.product/Product
  [{:product/keys [name slug price] :as model} :- models.product/Product]
  (log/info (str ".model->domain" model))
  {:id    (:db/id model)
   :name  name
   :slug  slug
   :price price})