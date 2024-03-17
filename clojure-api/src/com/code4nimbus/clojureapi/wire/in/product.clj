(ns com.code4nimbus.clojureapi.wire.in.product
  (:require [schema.core :as s]))

(s/defschema Product
  {:name  s/Str
   :slug  s/Str
   :price s/Num})

(s/defschema ProductUpdate
  {:id    s/Num
   :name  s/Str
   :slug  s/Str
   :price s/Num})
