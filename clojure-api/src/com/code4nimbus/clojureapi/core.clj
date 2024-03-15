(ns com.code4nimbus.clojureapi.core
  (:require [compojure.api.sweet :refer [api routes]]
            [com.code4nimbus.clojureapi.diplomat.product :refer [product-routes]]
            [org.httpkit.server :refer [run-server]]
            [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.datomic.db :as db])
  (:gen-class))

(def swagger-config
  {:ui      "/swagger"
   :spec    "/swagger.json"
   :options {:ui   {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(def app (api {:swagger swagger-config} (apply routes product-routes)))

; starting point of server.
(defn -main [& args]
  (db/drop-database)
  (db/create-database)
  (db/create-schema)
  (run-server app {:port 9000})
  (log/info "Server started on port 9000"))