(ns clojure-studies.playbook.insane)

(def products [{:id          1
                :name        "Kettle"
                :type        :appliance
                :stock-count 50
                :unit-size   :small
                :price       100.40
                :status      :active}
               {:id          2
                :name        "Couch"
                :type        :furniture
                :stock-count 20
                :unit-size   :large
                :price       4599.00
                :status      :active}
               {:id          3
                :name        "Desk"
                :type        :furniture
                :stock-count 40
                :unit-size   :medium
                :price       1299.99
                :status      :active}
               {:id          4
                :name        "Toaster"
                :type        :appliance
                :stock-count 100
                :unit-size   :small
                :price       499.99
                :status      :active}
               {:id          5
                :name        "Bed"
                :type        :furniture
                :stock-count 10
                :unit-size   :large
                :price       120
                :status      :active
                :special?    true}])

;This approach is very hard to understand =/
(defn product-to-discount [products]
  (first (reverse (sort-by :stock-count (filter #(= (:unit-size %) :large) (filter #(= (:type %) :furniture) products))))))

(println
  (product-to-discount products))

;Threading ... MUCH BETTER
;THREAD FIRST MACRO!!!!!!!!!!!!!!!!!!!

(defn product-to-discount-threading [products]
  (->> products
      (filter #(= (:type %) :furniture))
      (filter #(= (:unit-size %) :large))
      (sort-by :stock-count)
      reverse
      first))


(println
  (product-to-discount-threading products))

;Threading ... MUCH BETTER
;THREAD LAST MACRO!!!!!!!!!!!!!!!!!!!

(defn get-product-by-id [id]
  (->> products
       (filter #(= (:id %) id ))
       first))
(defn sell-product [product buyer]
  (let [stock-count (:stock-count product)
        current-buyers (get :buyers product [])
        updated-buyers (conj current-buyers buyer)
        updated-stock-count (- stock-count 1)]
    (-> product
        (assoc :stock-count updated-stock-count)
        (assoc :buyers updated-buyers)))
  )

(println (sell-product (get-product-by-id 2) "Dan"))