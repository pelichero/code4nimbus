(ns com.code4nimbus.clojureapi.core
  (:require [compojure.api.sweet :refer [api routes]]
            [com.code4nimbus.clojureapi.diplomat.http-server :refer [product-routes]]
            [com.code4nimbus.clojureapi.diplomat.consumer :as diplomat.consumer]
            [com.code4nimbus.clojureapi.diplomat.producer :as diplomat.producer]
            [org.httpkit.server :refer [run-server]]
            [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.datomic.db :as db])
  (:gen-class))

(def swagger-config
  {:ui      "/swagger"
   :spec    "/swagger.json"
   :options {:ui   {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(defn ^:private configure-database
  []
  (log/info "Configuring database...")
  (db/drop-database)
  (db/create-database)
  (db/create-schema))

(defn ^:private configure-kafka
  []
  (log/info "Starting Kafka listeners...")
  (diplomat.consumer/start-kafka-listeners))

(def app (api {:swagger swagger-config} (apply routes product-routes)))

; starting point of server.
(defn -main [& args]
  (configure-database)
  (run-server app {:port 9000})
  (configure-kafka)
  (log/info "Server started on port 9000"))