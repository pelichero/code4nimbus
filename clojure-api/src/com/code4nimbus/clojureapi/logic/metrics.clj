(ns com.code4nimbus.clojureapi.logic.metrics
  (:require [compojure.api.sweet :refer :all]
            [iapetos.collector.jvm :as jvm]
            [iapetos.collector.ring :as ring]
            [iapetos.core :as prometheus]))

(defonce prometheus-registry
         (-> (prometheus/collector-registry)
             jvm/initialize
             ring/initialize
             (prometheus/register
               (prometheus/summary :code4nimbus-clojureapi/product-by-id-seconds)
               (prometheus/summary :code4nimbus-clojureapi/get-all-products-seconds)
               (prometheus/summary :code4nimbus-clojureapi/products-by-params-seconds)
               (prometheus/summary :code4nimbus-clojureapi/add-product-seconds)
               (prometheus/summary :code4nimbus-clojureapi/update-product-seconds)
               (prometheus/summary :code4nimbus-clojureapi/delete-product-seconds)
               (prometheus/summary :code4nimbus-clojureapi/generate-random-products-seconds)
               ;; Virtual-threads demo (see logic.loom): in-flight task count and
               ;; per-batch wall-clock, both split by thread mode.
               (prometheus/gauge :loom/inflight-tasks {:labels [:mode]})
               (prometheus/summary :loom/batch-seconds {:labels [:mode]}))))
