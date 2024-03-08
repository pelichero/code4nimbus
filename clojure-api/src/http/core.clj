(ns http.core
  (:require [compojure.api.sweet :refer [api routes]]
            [http.product :refer [product-routes]]
            [org.httpkit.server :refer [run-server]]
            [http.crud :as crud]
            [http.db :as db])
  (:use [clojure.pprint])
  (:gen-class))

(def swagger-config
  {:ui      "/swagger"
   :spec    "/swagger.json"
   :options {:ui   {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(def app (api {:swagger swagger-config} (apply routes product-routes)))

; starting point of server.
(defn -main [& args]
  (db/create-schema (crud/create-connection))
  (run-server app {:port 9000})
  (println "Server started on port 8080"))