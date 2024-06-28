(ns com.code4nimbus.clojureapi.components.app
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.diplomat.http-server :refer [product-routes prometheus-registry]]
            [com.stuartsierra.component :as component]
            [compojure.api.sweet :refer [api routes]]
            [iapetos.collector.ring :as ring]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [schema.core :as s]))

(def ^:private swagger-config
  {:ui      "/swagger"
   :spec    "/swagger.json"
   :options {:ui   {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "Restful CRUD API"}}}})

(defn ^:private wrap-metrics [app]
  (ring/wrap-metrics app
                     prometheus-registry
                     {:path    "/metrics"
                      :path-fn #(re-find #"^/[^/]+" (:uri %))}))

(s/defn ^:private app
  [db]
  (-> (api {:swagger swagger-config} (apply routes (product-routes db)))
      (wrap-defaults api-defaults)
      wrap-metrics))

(defrecord WebServer [db] component/Lifecycle
  (start [this]
    (log/info (str "Starting server... "))
    (assoc this :app
                (run-server (app db) {:port 9000})))
  (stop [this]
    (log/info "Stopping server...")
    this))