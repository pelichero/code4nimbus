(ns com.code4nimbus.clojureapi.domain.product
  (:require [schema.core :as s]))

(s/defschema NewProduct
  {:name  s/Str
   :slug  s/Str
   :price s/Num})

(s/defschema Product
  {:id    s/Num
   :name  s/Str
   :slug  s/Str
   :price s/Num})
