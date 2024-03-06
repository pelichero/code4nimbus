(ns http.core
  (:use clojure.pprint)
  (:require [org.httpkit.server :refer [run-server]]
            [clj-time.core :as t]
            [http.crud :as crud]
            [http.db :as db]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))
(import java.util.Date)

(defn initiate
  []
  (db/create-schema (crud/create-connection))
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "<h1>Welcome to Clojure HTTP server</h1>"})

; returns current time.
(defn get-time
  []
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (str (t/time-now))}]
    response))

; returns current date.
(defn get-date
  []
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (str (.getTime (java.util.Date.)))}]
    response))

(defn add-product
  []
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str (crud/add-product "New product" "/new-product" 100M))})

(defn list-products
  []
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body    (str (crud/get-products))}
        ]
    response))

; define routes.
(defroutes app
           (GET "/" [] (initiate))
           (GET "/get-time" [] (get-time))
           (GET "/get-date" [] (get-date))
           (GET "/add-product" [] (add-product))
           (GET "/list-products" [] (list-products))
           (route/not-found "<h1>Page not found</h1>"))

; starting point of server.
(defn -main [& args]
  (run-server app {:port 9000})
  (println "Server started on port 8080"))