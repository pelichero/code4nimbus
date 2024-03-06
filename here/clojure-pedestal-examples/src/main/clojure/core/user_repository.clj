(ns core.user-repository
  (:require [clojure.core.async :as async]))

(defonce repository (atom {}))

(def ^:private next-user-id
  (let [next (atom 0)]
    (fn []
      (swap! next inc)
      @next)))

(defn create-user [user]
  (let [user-id (next-user-id)
        user (assoc user :user-id user-id)]
    (println "Creating user " user)
    (swap! repository assoc user-id user)))

(defn find-user [user-id]
  (println "user-id [" user-id "]")
  (println "@repository " @repository)
  (get @repository user-id))


(defn find-users [] (vals @repository))

(defn find-users-async []
  (let [channel (async/chan)]
    (async/>!! channel (find-users))
    (async/close! channel)
    channel))

