(ns com.code4nimbus.clojureapi.adapters.product
  (:require [com.code4nimbus.clojureapi.model.product :as models.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [schema.core :as s]))

(s/defn wire->model :- models.product/Product
  [{:keys [name slug price]} :- wire.in.product/Product]
  {:product/name  name
   :product/slug  slug
   :product/price price})
