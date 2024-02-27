(ns http.core
  (:require [org.httpkit.server :refer [run-server]]
            [clj-time.core :as t]
            ;[http.crud :as crud]
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))
(import java.util.Date)

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

(defn datomic
  []
  (let [response {:status  200
                  :headers {"Content-Type" "text/html"}
                  :body          ""                           ;;(str (crud/db-after-create))
                  }]
    response))

; define routes.
(defroutes app
           (GET "/" [] "<h1>Welcome</h1>")
           (GET "/get-time" [] (get-time))
           (GET "/get-date" [] (get-date))
           (GET "/datomic" [] (datomic))
           (route/not-found "<h1>Page not found</h1>"))

; starting point of server.
(defn -main [& args]
  (run-server app {:port 8080})
  (println "Server started on port 8080"))