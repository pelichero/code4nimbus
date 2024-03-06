(ns clojure-studies.playbook.testing)


(def prices [20 40 50])

(println (get prices 2))

(println (map prices))

(defn apply-definitions
  [valor]
  (println "Apply value to this.")
  (+ valor 10))

(println (update prices 0 apply-definitions))


(println (map apply-definitions prices))

(defn my-reduce-test
  [a, b]
  (println "Somando os valores " a "e " b)
  (+ a b))

(println (reduce my-reduce-test 0 prices))

