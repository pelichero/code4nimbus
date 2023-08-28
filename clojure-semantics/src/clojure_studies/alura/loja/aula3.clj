(ns clojure-studies.alura.loja.aula3
  (:require [clojure-studies.alura.loja.db :as db]
            [clojure-studies.alura.loja.logic :as logic]))

(let [resumo (logic/resumo-por-usuario (db/todos-pedidos))]
  (println resumo))