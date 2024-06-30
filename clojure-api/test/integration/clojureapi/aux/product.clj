(ns integration.clojureapi.aux.product
  (:require [clojure.data.json :as json]
            [clojure.test :refer :all]
            [clojure.walk :refer [keywordize-keys]]
            [integration.clojureapi.aux.init :refer [start-system-map]]
            [com.code4nimbus.clojureapi.diplomat.http-server :refer [product-routes]]
            [compojure.api.sweet :refer [api routes]]
            [peridot.core :refer :all]))

(def app (api (apply routes (product-routes (-> (start-system-map)
                                                :db)))))

(defn get-products
  []
  (-> (session app)
      (request "/products"
               :request-method :get
               :content-type "application/json")
      :response
      :body
      json/read-str
      first
      keywordize-keys))

(defn get-product-by-id
  [id]
  (-> (session app)
      (request (str "/product/" id)
               :request-method :get
               :content-type "application/json")
      :response
      :body
      json/read-str
      keywordize-keys))

(defn add-product
  [product]
  (-> (session app)
      (request "/product"
               :request-method :post
               :content-type "application/json"
               :body (json/write-str product))
      :response
      :body
      json/read-str
      keywordize-keys))

(defn delete-product
  [id]
  (-> (session app)
      (request (str "/product/" id)
               :request-method :delete
               :content-type "application/json")
      :response
      :body))

(defn update-product
  [id
   product]
  (-> (session app)
      (request (str "/product/" id)
               :request-method :put
               :content-type "application/json"
               :body (json/write-str product))
      :response
      :body
      json/read-str
      keywordize-keys))