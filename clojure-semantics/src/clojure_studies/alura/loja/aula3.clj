(ns clojure-studies.alura.loja.aula3
  (:require [clojure-studies.alura.loja.db :as db]))

(defn valor-total [{:keys [quantidade]}]
  (->> quantidade
       (reduce +)
       println))

(valor-total (db/todos-pedidos))