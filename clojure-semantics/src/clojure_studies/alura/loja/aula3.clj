(ns clojure-studies.alura.loja.aula3
  (:require [clojure-studies.alura.loja.db :as db]))

(defn total-do-item
  [[_ detalhes]]
  (* (get detalhes :quantidade 0) (get detalhes :preco-unitario 0)))

(defn total-do-pedido
  [pedido]
  (reduce + (map total-do-item pedido)))

(defn total-dos-pedidos
  [pedidos]
  (->> pedidos
    (map :itens)
    (map total-do-pedido)
    (reduce +)))

(defn quantidade-de-pedidos-e-gasto-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id usuario
   :total-de-pedidos (count pedidos)
   :preco-total (total-dos-pedidos pedidos)})

(->> (db/todos-pedidos)
     (group-by :usuario)
     (map quantidade-de-pedidos-e-gasto-total-por-usuario)
     println)