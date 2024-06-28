(ns com.code4nimbus.clojureapi.components.message
  (:require [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.diplomat.consumer :as diplomat.consumer]))

(defn ^:private configure-kafka
  [conn]
  (log/info "Starting Kafka listeners...")
  (diplomat.consumer/product-listener conn))