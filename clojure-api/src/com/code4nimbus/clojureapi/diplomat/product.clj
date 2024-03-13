(ns com.code4nimbus.clojureapi.diplomat.product
  (:require [compojure.api.sweet :refer :all]
            [com.code4nimbus.clojureapi.controller.product :as controller.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojureapi.datomic.db :as datomic.db]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [schema.core :as s])
  (:use [clojure.pprint]))
(import java.util.Date)

(s/defn ^:private add!
  [conn
   product :- wire.in.product/Product]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (-> (adapters.product/wire->domain product)
                (controller.product/add! conn)
                (adapters.product/domain->wire))})

(s/defn ^:private get-all
  [conn]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str (controller.product/get-all conn))})

(s/defn ^:private by-params
  [conn
   name]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str (controller.product/by-name conn name))})

(def product-routes
  [(GET "/products" []
     (get-all (datomic.db/get-conn)))
   (GET "/products-by-params" []                            ;TODO refactor (test)
     :query-params [name :- (describe String "Products name.")]
     (by-params (datomic.db/get-conn) name))
   (POST "/product" []
     :body [create-product-req wire.in.product/Product]
     (add! (datomic.db/get-conn) create-product-req))])