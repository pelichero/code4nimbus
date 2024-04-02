(ns integration.clojureapi.aux.matcher
  (:require [clojure.test :refer :all]))

(defn- match-keys? [m k]
  (every? #(contains? m %) k))

(defn- match-types? [m t]
  (every? (fn [[k v]]
            (if-let [type-checker (get t k)]
              (type-checker v)
              true))
          m))

(defn match? [m p]
  (and (match-keys? m (keys p))
       (match-types? m p)))