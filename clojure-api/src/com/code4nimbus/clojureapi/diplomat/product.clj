(ns com.code4nimbus.clojureapi.diplomat.product
  (:require [compojure.api.sweet :refer [GET POST]]
            [com.code4nimbus.clojureapi.controller.product :as controller.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojureapi.datomic.db :as datomic.db]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [schema.core :as s])
  (:use [clojure.pprint]))
(import java.util.Date)

(s/defn ^:private add-product
  [conn
   product :- wire.in.product/Product]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (-> (adapters.product/wire->model product)
                (controller.product/add! conn)
                str)})                                      ;TODO create wire.out

(s/defn ^:private list-products
  [conn]
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (str (controller.product/get-all conn))}]
    response))

(def product-routes
  [(GET "/products" []
     (list-products (datomic.db/get-conn)))
   (POST "/product" []
     :body [create-product-req wire.in.product/Product]
     (add-product (datomic.db/get-conn) create-product-req))])