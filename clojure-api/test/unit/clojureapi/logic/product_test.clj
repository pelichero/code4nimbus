(ns com.code4nimbus.clojureapi.logic.product-test
  (:require [clojure.test :refer :all])
  (:require [com.code4nimbus.clojureapi.logic.product :refer [->random-product]]))

(deftest ->random-product-test
  (testing "->random-product"
    (let [product (->random-product)]
      (is (map? product))
      (is (contains? product :name))
      (is (contains? product :slug))
      (is (contains? product :price))
      (is (string? (:name product)))
      (is (string? (:slug product)))
      (is (integer? (:price product))))))
