(ns com.code4nimbus.clojureapi.diplomat.http-server
  (:require [clojure.data.json :as json]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [com.code4nimbus.clojureapi.controller.product :as controller.product]
            [com.code4nimbus.clojureapi.logic.metrics :refer [prometheus-registry]]
            [com.code4nimbus.clojureapi.wire.in.product :as wire.in.product]
            [compojure.api.sweet :refer :all]
            [iapetos.core :as prometheus]
            [schema.core :as s]))
(import java.util.Date)

(s/defn ^:private add-product!
  [conn
   product :- wire.in.product/Product]
  {:status  200
   :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
   :body    (-> (adapters.product/wire->domain product)
                (controller.product/add! conn)
                (adapters.product/domain->wire)
                (json/write-str))})

(s/defn ^:private update-product!
  [conn
   product :- wire.in.product/ProductUpdate]
  {:status  201
   :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
   :body    (-> (adapters.product/wire-update->domain product)
                (controller.product/update! conn)
                (adapters.product/domain->wire)
                (json/write-str))})

(s/defn ^:private get-all-products
  [conn]
  (let [products (controller.product/get-all conn)]
    {:status  200
     :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
     :body    (-> (map adapters.product/domain->wire products)
                  (json/write-str))}))

(s/defn ^:private products-by-params
  [conn
   name]
  (let [products (controller.product/by-name conn name)]
    {:status  200
     :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
     :body    (-> (map adapters.product/domain->wire products)
                  (json/write-str))}))

(s/defn ^:private product-by-id
  [conn
   id]
  (let [product (controller.product/by-id conn id)]
    (cond (nil? product)
          {:status  404
           :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
           :body    "Product not found."}
          :else
          {:status  200
           :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
           :body    (-> (adapters.product/domain->wire product)
                        (json/write-str))})))

(s/defn ^:private delete-product!
  [conn
   id]
  (controller.product/delete! conn id)
  {:status  204
   :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
   :body    "Product deleted."})

(s/defn ^:private generate-random-products!
  [conn
   num]
  (let [products (controller.product/generate-random-products conn num)]
    {:status  201
     :headers {"Content-Type" "text/html", "Access-Control-Allow-Origin" "*"}
     :body    (-> (map adapters.product/domain->wire products)
                  (json/write-str))}))

(s/defn product-routes
  [{:keys [connection]}]
  [(GET "/product/:id" []
     :path-params [id :- Long]
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (product-by-id connection id)))
   (GET "/products" []
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (get-all-products connection)))
   (GET "/products-by-params" []
     :query-params [name :- (describe String "Products name.")]
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (products-by-params connection name)))
   (POST "/product" []
     :body [create-product-req wire.in.product/Product]
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (add-product! connection create-product-req)))
   (PUT "/product/:id" []
     :path-params [id :- Long]
     :body [update-product-req wire.in.product/Product]
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (update-product! connection (assoc update-product-req :id id))))
   (PUT "/product/generate-random/:num" []
     :path-params [num :- Long]
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (generate-random-products! connection num)))
   (DELETE "/product/:id" []
     :path-params [id :- Long]
     (prometheus/with-duration
       (prometheus-registry :code4nimbus-clojureapi/delete-product-seconds)
       (delete-product! connection id)))])