(ns com.code4nimbus.clojure-async.core
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojure-async.diplomat.http-server :refer [product-routes]]
            [compojure.api.sweet :refer [api routes]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(def swagger-config
  {:ui      "/swagger"
   :spec    "/swagger.json"
   :options {:ui   {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(def app (api {:swagger swagger-config} (apply routes product-routes)))

; starting point of server.
(defn -main [& args]
  (run-server app {:port 9001})
  (log/info "Server started on port 9001"))