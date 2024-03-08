(ns http.product
  (:require [compojure.api.sweet :refer [GET POST]]
            [http.crud :as crud]
            [clojure.core :refer :all]
            [schema.core :as s])
  (:use [clojure.pprint]))
(import java.util.Date)

(s/defschema ProductRequestSchema
  {:name  s/Str
   :slug  s/Str
   :price bigdec})

(defn ^:private add-product
  [{:keys [name slug price]}]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str (crud/add-product name slug price))})

(defn ^:private list-products
  []
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (str (crud/get-products))}
        ]
    response))

(def product-routes
  [(GET "/products" []
     (list-products))
   (POST "/product" []
     :body [create-product-req ProductRequestSchema]
     (add-product create-product-req))])