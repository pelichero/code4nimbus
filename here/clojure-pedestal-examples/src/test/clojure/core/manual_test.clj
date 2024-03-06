(ns core.manual-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]))

(defn run-tests []
  (let [result (io.pedestal.test/response-for service :get "/")]
    (println result)))
