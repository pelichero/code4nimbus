(ns com.code4nimbus.clojureapi.logic.product
  (:require [namejen.names :as n]
            [com.code4nimbus.clojureapi.domain.product :as domain.product]
            [schema.core :as s]))

(defn ^:private kebab-case [s]
  (->> s
       (clojure.string/lower-case)
       (clojure.string/replace #"[\s_]+" "-")))

(s/defn ->random-product :- domain.product/NewProduct
  []
  (let [name (n/name-maker)]
    {:name  name
     :slug  (str "product/" (kebab-case name))
     :price (rand-int 100)}))