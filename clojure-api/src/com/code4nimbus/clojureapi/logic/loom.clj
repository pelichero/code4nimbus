(ns com.code4nimbus.clojureapi.logic.loom
  "Load generator used to compare platform threads vs JDK 21 virtual threads.
   Fans out N blocking jobs (each sleeps to simulate I/O) on an executor chosen
   by `mode`, and instruments the in-flight task count and batch wall-clock so
   the contrast is visible in Prometheus/Grafana."
  (:require [clojure.string :as str]
            [com.code4nimbus.clojureapi.logic.metrics :refer [prometheus-registry]]
            [iapetos.core :as prometheus])
  (:import (java.util.concurrent Callable ExecutorService Executors Future)))

;; Default mode for this JVM, set per container via docker-compose.
(def ^:private container-mode
  (or (System/getenv "THREAD_MODE") "platform"))

(defn ^:private normalize-mode
  "An explicit ?mode= wins; otherwise fall back to this container's THREAD_MODE."
  [mode]
  (let [m (some-> mode str/lower-case str/trim)]
    (if (#{"platform" "virtual"} m) m container-mode)))

(defn ^:private ^ExecutorService executor-for
  [mode n-tasks]
  (if (= "virtual" mode)
    (Executors/newVirtualThreadPerTaskExecutor)
    ;; A bounded pool of real OS threads: this is what saturates under load and
    ;; makes jvm_threads_current spike while jobs queue up.
    (Executors/newFixedThreadPool (max 1 (min (int n-tasks) 200)))))

(defn run-batch!
  "Run `n-tasks` blocking jobs (each sleeps `sleep-ms` ms) on a platform or
   virtual executor; block until all finish; return a result map. Holds
   loom_inflight_tasks{mode} up while jobs run and records loom_batch_seconds{mode}."
  [mode n-tasks sleep-ms]
  (let [mode     (normalize-mode mode)
        n-tasks  (max 1 (int n-tasks))
        sleep-ms (max 0 (long sleep-ms))
        inflight (prometheus-registry :loom/inflight-tasks {:mode mode})
        pool     (executor-for mode n-tasks)]
    (try
      (prometheus/with-duration
        (prometheus-registry :loom/batch-seconds {:mode mode})
        (let [start (System/nanoTime)
              jobs  (mapv (fn [_]
                            (reify Callable
                              (call [_]
                                (prometheus/inc inflight)
                                (try (Thread/sleep sleep-ms) "ok"
                                     (finally (prometheus/dec inflight))))))
                          (range n-tasks))]
          (doseq [^Future f (.invokeAll pool jobs)]
            (.get f))
          {:mode      mode
           :tasks     n-tasks
           :sleepMs   sleep-ms
           :elapsedMs (/ (- (System/nanoTime) start) 1e6)}))
      (finally
        (.shutdown pool)))))
