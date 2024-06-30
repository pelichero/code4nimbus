(ns integration.clojureapi.product_test
  (:require [clojure.test :refer :all]
            [integration.clojureapi.aux.matcher :refer [match?]]
            [integration.clojureapi.aux.product :as aux.product]
            [peridot.core :refer :all]))

(deftest get-products-test
  (testing "GET products test"
    (let [product {:name  "test"
                   :slug  "test"
                   :price 1000}
          _ (aux.product/add-product product)
          response (aux.product/get-products)]
      (is (match? response
                  {:id    integer?
                   :name  string?
                   :slug  string?
                   :price integer?})))))

(deftest get-products-by-id-test
  (testing "GET products by id test"
    (let [product {:name  "test"
                   :slug  "test"
                   :price 1000}
          add-product-response (aux.product/add-product product)
          response (aux.product/get-product-by-id (:id add-product-response))]
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

(deftest update-product-test
  (testing "PUT product test"
    (let [product {:name  "test"
                   :slug  "test"
                   :price 1000}
          response (aux.product/add-product product)
          update-response (aux.product/update-product (:id response) product)]
      (is (match? update-response
                  {:id    integer?
                   :name  string?
                   :slug  string?
                   :price integer?})))))