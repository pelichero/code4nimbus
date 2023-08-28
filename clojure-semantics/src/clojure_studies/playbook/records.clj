(ns clojure-studies.playbook.records)

{ :customer-id 12345
 :first-name  "Maria"
 :last-name   "da Silva" }

; an Account
{ :account-id "12345-01"
 :account-type :checking
 }

; Currency
{ :divisor 100
 :symbol "USD" }


(def currencies {
                 :usd {:divisor 100 :symbol "USD"}
                 :brl {:divisor 100 :symbol "BRL"}
                 })

{
 :amount 1200
 :currency :usd
 }

{
 :transaction-type :debit
 :details {,,,}
 :account-id "123456-02"
 :timestamp 1654530232597
 :amount {
          :amount 1200
          :currency :usd
          }
 }

(def accounts {
                "12345-01" {
                            :account-id "12345-01"
                            :account-type :checking}
                "12345-02" {
                            :account-id "12345-02"
                            :account-type :credit}
                })

(def customers {
                12345 {
                       :customer-id 12345
                       :name "Frodo"
                       :accounts [
                                  "12345-01"
                                  "12345-02"
                                  ]}
                12346 {
                       :customer-id 12346
                       :name "Sam"
                       :accounts [
                                  "123456-01"
                                  ]
                       }
                })




(defn test-customers
  []
  (println customers))

(test-customers)