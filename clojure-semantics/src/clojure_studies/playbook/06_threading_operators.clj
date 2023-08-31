(ns clojure-studies.playbook.06-threading-operators)


(defn hire
  [employee]
  (assoc (update (assoc employee
                   :email (str (:first-name employee) "@nubank.com.br"))
                 :employment-history conj "Nubank")
    :hired-at (java.util.Date.)))

(println (hire {:first-name "felipe"}))

(defn hire
  [employee]
  (let [email (str (:first-name employee) "@nubank.com.br")
        employee-with-email (assoc employee :email email)
        employee-with-history (update employee-with-email :employment-history conj "Nubank")]
    (assoc employee-with-history :hired-at (java.util.Date.))))

(println (hire {:first-name "felipe"}))

; using Thread First
(defn hire
  [employee]
  (-> employee
      (assoc :email (str (:first-name employee) "@nubank.com.br"))
      (update :employment-history conj "Nubank")
      (assoc :hired-at (java.util.Date.))))

(hire {:first-name "Rich" :last-name "Hickey" :employment-history ["Cognitect"]})
;; => {:first-name "Rich",
;;     :last-name "Hickey",
;;     :employment-history ["Cognitect" "Nubank"],
;;     :email "Rich@nubank.com.br",
;;     :hired-at #inst "2022-05-20T19:41:54.498-00:00"}


; using Thread Last

(def employees
  [{:first-name "A" :last-name "B" :email "A@nubank.com.br" :hired-at #inst "2022-05-20"}
   {:first-name "C" :last-name "D" :email "C@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "E" :last-name "F" :email "E@nubank.com.br" :hired-at #inst "2022-05-21"}

   ;; Your fix was implemented and deployed here

   {:first-name "G" :last-name "H" :email "G.H@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "I" :last-name "J" :email "I.J@nubank.com.br" :hired-at #inst "2022-05-22"}])

;; Current DB of hired employees
(def employees
  [{:first-name "A" :last-name "B" :email "A@nubank.com.br" :hired-at #inst "2022-05-20"}
   {:first-name "C" :last-name "D" :email "C@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "E" :last-name "F" :email "E@nubank.com.br" :hired-at #inst "2022-05-21"}

   ;; Your fix was implemented and deployed here

   {:first-name "G" :last-name "H" :email "G.H@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "I" :last-name "J" :email "I.J@nubank.com.br" :hired-at #inst "2022-05-22"}])

(defn email-address
  "Return email address containing first and last name."
  [employee]
  (format "%s.%s@nubank.com.br"
          (:first-name employee)
          (:last-name employee)))

(defn old-email-format?
  "Return true when employee email does not follow the new format."
  [employee]
  (not= (:email employee) (email-address employee)))

(defn hired-day
  "Return the day of hire from :hired-at."
  [employee]
  (.getDay (:hired-at employee)))

(defn report
  [employees]
  (->> employees
       (filter old-email-format?)
       (map #(assoc % :email (email-address %)))
       (group-by hired-day)
       vals))

(println (report employees))