(ns com.code4nimbus.clojureapi.core
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.datomic.db :as datomic.db]
            [com.code4nimbus.clojureapi.diplomat.consumer :as diplomat.consumer]
            [com.code4nimbus.clojureapi.diplomat.http-server :refer [product-routes registry]]
            [compojure.api.sweet :refer [api routes]]
            [iapetos.collector.ring :as ring]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]])
  (:gen-class))

(def swagger-config
  {:ui      "/swagger"
   :spec    "/swagger.json"
   :options {:ui   {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(defn ^:private configure-database
  []
  (log/info "Configuring database...")
  (datomic.db/configure))

(defn ^:private configure-kafka
  [conn]
  (log/info "Starting Kafka listeners...")
  (diplomat.consumer/product-listener conn))

(defn ^:private wrap-metrics [app]
  (ring/wrap-metrics app
                     registry
                     {:path    "/metrics"
                      :path-fn #(re-find #"^/[^/]+" (:uri %))}))

(def app
  (-> (api {:swagger swagger-config} (apply routes product-routes))
      (wrap-defaults api-defaults)
      wrap-metrics))

(defonce server (atom nil))

(defn -main [& _]
  (let [conn (configure-database)]
    (reset! server (run-server app {:port 9000}))
    (configure-kafka conn)
    (log/info "Server started on port 9000")))