(ns com.code4nimbus.clojure-async.domain.product
  (:require [schema.core :as s]))

(s/defschema NewProduct
  {:name  s/Str
   :slug  s/Str
   :price s/Num})