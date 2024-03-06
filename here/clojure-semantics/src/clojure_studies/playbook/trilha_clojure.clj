(ns clojure-studies.playbook.trilha_clojure)
(defn hello-world
  [name]
  (str "Hello " name)
  )

(hello-world "Teste")


(do (map #(str "Hello " %1) ["Inez" "Maria" "Fred" "Joao"]))


(do
  (println "foo")
  "bar")




(defrecord Person [first-name,
                     last-name])

(defn test-records
  [name, last-name]
  (let [person (->Person name last-name)]
  (println person)))
(test-records "Frodo" "Baggins")
