(ns com.code4nimbus.clojureapi.diplomat.consumer
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [com.code4nimbus.clojureapi.adapters.product :as adapters.product]
            [com.code4nimbus.clojureapi.controller.product :as controller.product]
            [schema.core :as s]))

(s/defn product-handler
  [record
   db]
  (log/info (str "Processed Value: " (.value record)))
  (try
    (let [product-persisted (-> (json/read-json (.value record))
                                (adapters.product/wire->domain)
                                (controller.product/add! (:connection db)))]
      (log/info (str "Product persisted: " product-persisted)))
    (catch Exception e
      (log/error e))))