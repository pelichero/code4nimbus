(ns clojure-studies.playbook.lists)

(def numbers [1 2 3 4 5 6 7])

(println
  (->> numbers
       (filter odd?)
       (map #(* % %))
       (clojure.string/join ", ")))

(->> numbers
     (reduce +)
     println)


(defn fibonacci
     ([number]
      (fibonacci 0, number))
     ([i, number]
      (if (< i number)
           (println i " - " number)
           (recur i (number * (fibonacci  number))))))

(defn fib
     ([n]
      (fib [0 1] n))
     ([x, n]
      (if (< (count x) n)
           (fib (conj x (+ (last x) (nth x (- (count x) 2)))) n)
           x)))

(println (fib 10))