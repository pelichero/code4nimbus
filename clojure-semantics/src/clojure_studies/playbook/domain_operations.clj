(ns clojure-studies.playbook.domain-operations
  (:require [clojure.spec.alpha :as s]))

:Currency

{ :divisor 100
 :symbol "USD"}

(def currencies {
                 :usd { :divisor 100 :code "USD" :sign "$" :desc "US Dollars"}
                 :brl { :divisor 100 :code "BRL" :sign "R$" :desc "Brazilian Real"}
                 :ukg { :divisor (* 17 29) :code "UKG" :sign "ʛ" :desc "Galleons of the United Kingdom"}})

{ :amount 1200
 :currency :usd }

{ :transaction-type :debit
 :account-id "12345-01"
 :details {   }
 :timestamp 1654530232597
 :amount { :amount 1200
          :currency :usd }
 }

(def default-currency (:brl currencies))

(defn make-money
  ([] {:amount 0
       :currency default-currency})
  ([amount] {:amount amount
             :currency default-currency})
  ([amount currency] {:amount amount
                      :currency currency}))

(def zero-money {:amount 0 :currency default-currency})

(defn show-galleons
  "creates a display string for Harry Potter money"
  [amount]
  (let [{:keys [divisor code sign desc]} (:ukg currencies)
        galleons      (int (/ amount divisor))
        less-galleons (rem amount divisor)
        sickles       (int (/ less-galleons 17))
        knuts         (rem less-galleons 29)]
    (str galleons " Galleons, " sickles " Sickles, and " knuts " Knuts")))

(defn show-money
  "creates a display string for a Money entity"
  [{:keys [amount currency]}]
  (let [{:keys [divisor code sign desc]} currency]
    (cond
      (= code "UKG")
      (show-galleons amount)
      :else
      (let [major (int (/ amount divisor))
            minor (mod amount divisor)]
        (str sign major "." minor)))))

(def default-currency (:brl currencies))

(defn make-money
  "takes an amount and a currency, creating a Money entity"
  ([]                   {:amount 0
                         :currency default-currency})
  ([amount]             {:amount amount
                         :currency default-currency})
  ([amount currency]    {:amount amount
                         :currency currency}))


;(println (make-money 525 (:ukg currencies)))

(s/def :money/amount int?)
(s/def :currency/divisor int?)
(s/def :currency/sign string?)
(s/def :currency/desc string?)

(s/def :currency/code (and string? #{"USD" "BRL" "UKG" ,,, }))
