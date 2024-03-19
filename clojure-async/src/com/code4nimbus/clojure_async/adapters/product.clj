(ns com.code4nimbus.clojure-async.adapters.product
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojure-async.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojure-async.domain.product :as domain.product]
            [schema.core :as s]))

(s/defn wire->domain :- domain.product/NewProduct
  [{:keys [name slug price] :as wire} :- wire.in.product/Product]
  (log/info (str "update->domain" wire))
  {:name  name
   :slug  slug
   :price price})