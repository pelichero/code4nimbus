(ns integration.clojureapi.product
  (:require [clojure.data.json :as json]
            [clojure.test :refer :all]
            [com.code4nimbus.clojureapi.diplomat.http-server :refer [product-routes]]
            [compojure.api.sweet :refer [api routes]]
            [peridot.core :refer :all]))

(def app (api (apply routes product-routes)))

;TODO finalize this test
(deftest products-test
  (testing "GET products test"
    (let [response (-> (session app)
                       (request "/products"
                                :request-method :get
                                :content-type "application/json")
                       :response
                       :body
                       json/read-str
                       first)]
      ;(is (= {:id   integer?
      ;        :name string?
      ;        :slug string?}
      ;       response))
      )))