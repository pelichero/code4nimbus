(ns com.code4nimbus.clojure-async.diplomat.http-server
  (:require [clojure.data.json :as json]
            [com.code4nimbus.clojure-async.adapters.product :as adapters.product]
            [com.code4nimbus.clojure-async.controller.product :as controller.product]
            [com.code4nimbus.clojure-async.wire.in.product :as wire.in.product]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]))
(import java.util.Date)

(s/defn ^:private add-product!
  [product :- wire.in.product/Product]
  (try
    (let [_ (-> (adapters.product/wire->domain product)
                (controller.product/send!))]
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    {}}
      )
    (catch Exception e
      {:status  500
       :headers {"Content-Type" "text/html"}
       :body    (json/write-str {:error (.getMessage e)})}
      )))

(def product-routes
  [(POST "/product" []
     :body [create-product-req wire.in.product/Product]
     (add-product! create-product-req))])