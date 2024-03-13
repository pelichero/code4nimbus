(ns com.code4nimbus.clojureapi.adapters.product
  (:require [com.code4nimbus.clojureapi.model.product :as models.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojureapi.wire.out.product :as wire.out.product]
            [com.code4nimbus.clojureapi.domain.product :as domain.product]
            [schema.core :as s]))

;TODO refactor it

(s/defn wire->domain :- wire.out.product/Product
  [{:keys [name slug price]} :- wire.in.product/Product]
  {:name  name
   :slug  slug
   :price price})

(s/defn domain->wire :- wire.out.product/Product
  [{:keys [name slug price]} :- domain.product/Product]
  {:name  name
   :slug  slug
   :price price})

(s/defn domain->model :- models.product/Product
  [{:keys [name slug price]} :- domain.product/Product]
  {:product/name  name
   :product/slug  slug
   :product/price price})

(s/defn model->domain :- domain.product/Product
  [model :- models.product/Product]
  (println model)
  {:name  (:product/name model)
   :slug  (:product/slug model)
   :price (:product/price model)})