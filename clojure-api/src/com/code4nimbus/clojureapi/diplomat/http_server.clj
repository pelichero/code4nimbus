(ns com.code4nimbus.clojureapi.diplomat.http-server
  (:require [compojure.api.sweet :refer :all]
            [com.code4nimbus.clojureapi.controller.product :as controller.product]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [com.code4nimbus.clojureapi.datomic.db :as datomic.db]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [clojure.data.json :as json]
            [schema.core :as s]))
(import java.util.Date)

(s/defn ^:private add-product!
  [conn
   product :- wire.in.product/Product]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (-> (adapters.product/wire->domain product)
                (controller.product/add! conn)
                (adapters.product/domain->wire)
                (json/write-str))})

(s/defn ^:private update-product!
  [conn
   product :- wire.in.product/ProductUpdate]
  {:status  201
   :headers {"Content-Type" "text/html"}
   :body    (-> (adapters.product/wire-update->domain product)
                (controller.product/update! conn)
                (adapters.product/domain->wire)
                (json/write-str))})

(s/defn ^:private get-all-products
  [conn]
  (let [products (controller.product/get-all conn)]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (-> (map adapters.product/domain->wire products)
                  (json/write-str))}))

(s/defn ^:private products-by-params
  [conn
   name]
  (let [products (controller.product/by-name conn name)]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (-> (map adapters.product/domain->wire products)
                  (json/write-str))}))

(s/defn ^:private product-by-id
  [conn
   id]
  (let [product (controller.product/by-id conn id)]
    (cond (nil? product)
          {:status  404
           :headers {"Content-Type" "text/html"}
           :body    "Product not found."}
          :else
          {:status  200
           :headers {"Content-Type" "text/html"}
           :body    (-> (adapters.product/domain->wire product)
                        (json/write-str))})))

(s/defn ^:private delete-product!
  [conn
   id]
  (controller.product/delete! conn id)
  {:status  204
   :headers {"Content-Type" "text/html"}
   :body    "Product deleted."})

(def product-routes
  [(GET "/product/:id" []
     :path-params [id :- Long]
     (product-by-id (datomic.db/get-conn) id))
   (GET "/products" []
     (get-all-products (datomic.db/get-conn)))
   (GET "/products-by-params" []
     :query-params [name :- (describe String "Products name.")]
     (products-by-params (datomic.db/get-conn) name))
   (POST "/product" []
     :body [create-product-req wire.in.product/Product]
     (add-product! (datomic.db/get-conn) create-product-req))
   (PUT "/product/:id" []
     :path-params [id :- Long]
     :body [update-product-req wire.in.product/Product]
     (update-product! (datomic.db/get-conn) (assoc update-product-req :id id)))
   (DELETE "/product/:id" []
     :path-params [id :- Long]
     (delete-product! (datomic.db/get-conn) id))])