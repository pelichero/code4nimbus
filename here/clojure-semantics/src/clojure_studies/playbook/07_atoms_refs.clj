(ns clojure-studies.playbook.07-atoms-refs)
(def learn-atom (atom 0))
(println (+ (deref learn-atom) 1))
(println (+ @learn-atom 1))
(println (swap! learn-atom inc))
(println (swap! learn-atom + 9))

(println (swap! learn-atom + 9))

(def learn-ref (ref ["red" "green"]))


(println (conj @learn-ref "blue"))