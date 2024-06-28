(ns com.code4nimbus.clojureapi.core
  (:require [com.code4nimbus.clojureapi.components :refer [system]]
            [com.stuartsierra.component :as component])
  (:gen-class))

(defn -main
  [& _]
  (component/start (system)))