(ns clojure-studies.playbook.testing-map-destruct)
(def products {
               :chair {:price 1000 :qtd 2}
               :table {:price 1000 :qtd 2}})

(defn print_15 [[key value]]
  (println key ": " value )
          15)

(println (map print_15 products))


(defn price-per-product [[_, value]]
  (* (:qtd value) (:price value)))

(println (map price-per-product products))

(defn all-price-per-product
  [price]
  (reduce + (map price-per-product price)))
(println "Invoking map/reduce -> " (all-price-per-product products) )


; THREADING

(defn pricing-threading-test
  [products]
  (->> products
      (map price-per-product)
      (reduce +)))

(println "Threading reduce test" (pricing-threading-test products))