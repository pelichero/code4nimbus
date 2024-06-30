(ns integration.clojureapi.aux.init
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [com.code4nimbus.clojureapi.components :refer [system]]))

(defn start-system-map
  []
  (component/start (system)))