(ns http.product
  (:require [compojure.api.sweet :refer [GET POST]]
            [http.crud :as crud]
            [http.db :as db]
            [schema.core :as s])
  (:use [clojure.pprint]))
(import java.util.Date)

(s/defschema ProductRequestSchema
  {:name  s/Str
   :slug  s/Str
   :price s/Num})

(defn ^:private add-product
  [conn
   {:keys [name slug price]}]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str (crud/add-product conn name slug price))})

(defn ^:private list-products
  [conn]
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (str (crud/get-products conn))}
        ]
    response))

(def product-routes
  [(GET "/products" []
     (list-products (db/get-conn)))
   (POST "/product" []
     :body [create-product-req ProductRequestSchema]
     (add-product (db/get-conn) create-product-req))])