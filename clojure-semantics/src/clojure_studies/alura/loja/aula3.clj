(ns clojure-studies.alura.loja.aula3
  (:require [clojure-studies.alura.loja.db :as db]))


(defn conta-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id       usuario
   :total-de-pedidos (count pedidos)}
  )

(->> (db/todos-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)