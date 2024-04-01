(ns com.code4nimbus.clojureapi.adapters.product-test
  (:require [clojure.test :refer :all]
            [schema-generators.generators :as gen]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojureapi.domain.product :as domain.product]
            [com.code4nimbus.clojureapi.model.product :as model.product]))

(deftest wire->domain-test
  (testing "wire->domain"
    (let [wire (gen/generate wire.in.product/Product)]
      (is (= wire
             (adapters.product/wire->domain wire))))))

(deftest wire-update->domain-test
  (testing "wire-update->domain"
    (let [wire (gen/generate wire.in.product/ProductUpdate)]
      (is (= wire
             (adapters.product/wire-update->domain wire))))))

(deftest domain->wire-test
  (testing "domain->wire"
    (let [domain (gen/generate domain.product/Product)]
      (is (= domain
             (adapters.product/domain->wire domain))))))

(deftest update-domain->model-test
  (testing "update-domain->model"
    (let [domain (gen/generate domain.product/Product)]
      (is (= {:db/id         (:id domain)
              :product/name  (:name domain)
              :product/slug  (:slug domain)
              :product/price (:price domain)}
             (adapters.product/update-domain->model domain))))))

(deftest domain->model-test
  (testing "domain->model"
    (let [domain (gen/generate domain.product/NewProduct)]
      (is (= {:product/name  (:name domain)
              :product/slug  (:slug domain)
              :product/price (:price domain)}
             (adapters.product/domain->model domain))))))

(deftest model->domain-test
  (testing "model->domain"
    (let [model (-> (gen/generate model.product/Product)
                    (assoc :db/id 1))]
      (is (= {:id    (:db/id model)
              :name  (:product/name model)
              :slug  (:product/slug model)
              :price (:product/price model)}
             (adapters.product/model->domain model))))))