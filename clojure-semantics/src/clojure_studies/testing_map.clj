(ns clojure-studies.testing-map)

(def products {
               :tv       1000.1
               :notebook 999.00
               })
(defn playing-with-maps
  [map-to-play]
  (assoc map-to-play :chair 899))

(println (get (playing-with-maps products) :chair))
(println ((playing-with-maps products) :chair))
(def products (assoc products :chair 899))
(println "Testando update" (update products :chair inc))

(def complex-products {
                       :chair {:price 1000 :qtd 2}
                       :table {:price 1000 :qtd 2}})

(println "Working with multi-leveled maps " (:chair complex-products))

(println "Working with multi-leveled maps " (:qtd (:chair complex-products)))

(println "Nested update " (update-in complex-products [:chair :qtd] inc))


;THREADING!!!!

(println "Sample threading" (-> complex-products
                                :chair
                                :qtd))

(-> complex-products
    :chair
    :qtd
    println)



(def complex-products {
                       :chair {:price 1000 :qtd 2}
                       :table {:price 1000 :qtd 2}
                       :chaveiro {:qtd 1}})
(defn gratuito?
  [[_ value]]
  (<= (get value :price 0) 0))

(println (filter gratuito? complex-products))

(defn free?
  [item]
  (<= (get item :price 0) 0))

(println (filter (fn [[_, value]] (free? value)) complex-products))

(println (filter #(free? (second %)) complex-products))