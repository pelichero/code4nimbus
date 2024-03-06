(ns clojure-studies.alura.loja.easy-challenge)

(defn pet-to-human-age
  [x]
  (def pet {'cat 5 'dog 7 'fish 10 })
  (get pet x))

(defn age
  [petName petType petAge]
  (def ratio (pet-to-human-age petType))
  (println petName " is " (* petAge ratio) " years old in human years."))

(age "Fido" 'dog 10)