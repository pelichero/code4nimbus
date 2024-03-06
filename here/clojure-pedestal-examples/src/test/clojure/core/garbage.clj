(ns core.garbage
  (:require [clojure.test :refer :all]))

{:app {:tcp-ingest          (fn [] {:name "joe"})
       :from-json-to-domain (fn [json] json)
       :transform           (fn [x] x)
       :from-domain-to-json (fn [x] x)
       :sender              (fn [x] (println "Sent"))}}

;when your system is a pure function
(defn system1 [n] (inc n))

(def system1-plan
  [[1 2]
   [3 4]
   [5 7]])

(defn apply-plan-1 [plan system]
  (let [evaluate (fn [[in out]]
                   (let [result (= out (system in))]
                     [[in out] result]))]
    (map evaluate plan)))

;when the system is a function with memory. Every input produces an output
(def system2
  (let [memory (atom (list))]
    (fn [n]
      (swap! memory conj n)
      (apply + (take 2 @memory)))))

(defprotocol System2
  (feed [this input])
  (reset [this]))

(def system-2-instance
  (let [memory (atom (list))]
    (reify System2
      (feed [_ input]
        (swap! memory conj input)
        (apply + (take 2 @memory)))
      (reset [_]
        (reset! memory (list))))))


;;since it has a memory, it should be a series of input series.
;;between each series the system needs to reset
(def system2-plan
  [
   ;series 1
   [[1 1]
    [2 3]
    [5 7]]
   ;series 2
   [[1 1]
    [2 3]
    [5 7]]
   ]
  )

(defn apply-plan-2 [plan system]
  (let [evaluate (fn [[in out]]
                   (let [result (= out (system in))]
                     [[in out] result]))]
    (map evaluate plan)))


(def function-with-memory-plan
  [
   ;first sequence of input-output pairs
   [
    [1 "out1"]
    [2 "out12"]
    ]
   ;second sequence of input-output pairs
   [
    [3 "out3"]
    [4 "out34"]
    ;nil means no output
    [4 nil]
    ]
   ;and so on
   ])

;time can be interpreted as another input/output parameter
(def system-with-times
  [
   ;input at 08:15 produces an output a minute later
   [{:time "08:15" :input 1}
    {:time "08:16" :output "out1"}]]
  )


;multiple inputs (e.g an input stimulus (:in1) triggers a web service call whose return value (:in2) will be interpreted as an input)
(def system-with-times
  [
   ;input at 08:15 produces an output a minute later
   [{:time "08:15" :in1 1 :in2 4}
    {:time "08:16" :out "out1"}]]
  )

;multiple outputs, e.g. extra notification (email/slack), :out2
(def system-with-times
  [
   ;input at 08:15 produces an output a minute later
   [{:time "08:15" :in1 1 :in2 4}
    {:time "08:16" :out1 "out1" :out2 "some message"}]]
  )

;one input stimulus triggers multiple random number generation. the random numbers can be passed as a lazy sequence
(def system-with-times
  [
   ;input at 08:15 produces an output a minute later
   [{:time "08:15" :in1 1 :in2 4}
    {:time "08:16" :out1 "out1" :out2 "some message"}]]
  )