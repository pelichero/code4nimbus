(ns integration.clojureapi.product
  (:require [clojure.test :refer :all]
            [integration.clojureapi.aux.matcher :refer [match?]]
            [integration.clojureapi.aux.product :as aux.product]
            [peridot.core :refer :all]))

(deftest products-test
  (testing "GET products test"
    (let [response (aux.product/get-products)]
      (is (match? response
                  {:id    integer?
                   :name  string?
                   :slug  string?
                   :price integer?})))))

(deftest add-product-test
  (testing "POST product test"
    (let [product {:name  "test"
                   :slug  "test"
                   :price 1000}
          response (aux.product/add-product product)]
      (is (match? response
                  {:id    integer?
                   :name  string?
                   :slug  string?
                   :price integer?})))))

(deftest delete-product-test
  (testing "DELETE product test"
    (let [product {:name  "test"
                   :slug  "test"
                   :price 1000}
          response (aux.product/add-product product)
          id (:id response)
          delete-response (aux.product/delete-product id)]
      (is (= delete-response "Product deleted.")))))