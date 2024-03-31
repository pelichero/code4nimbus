(ns integration.clojureapi.product
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock.request]))

(deftest add-product!
  (testing "add-product!"
    (is (= (-> (mock.request/request :post "/product")
               (mock.request/body "{\"name\":\"test\",\"slug\":\"test\",\"price\":1}"))
           {:status  200
            :headers {"Content-Type" "text/html"}
            :body    "{\"name\":\"test\",\"slug\":\"test\",\"price\":1}"}))))
