(ns com.code4nimbus.clojureapi.wire.out.product
  (:require [schema.core :as s]))

(s/defschema Product
  {:id    s/Num
   :name  s/Str
   :slug  s/Str
   :price s/Num})
