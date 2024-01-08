(ns core.http-routes
  (:require
    [core.user-repository :as user-repository]
    [io.pedestal.http :as http]
    [io.pedestal.http.body-params :as body-params]
    [io.pedestal.http.route :as route]
    [ring.util.response :as ring-resp]
    [clojure.core.async :as async]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn ^:private to-http-response [domain-response]
  (if domain-response
    (ring-resp/response domain-response)
    (ring-resp/not-found nil)))

(defn find-user
  [request]
  (-> request
      (get-in [:path-params :user-id])
      read-string
      user-repository/find-user
      to-http-response))

(defn find-users
  [request]
  (ring-resp/response
      (user-repository/find-users)))

(defn find-users-async
  [request]
  (ring-resp/response
    (user-repository/find-users-async)))

(def find-users-async-2
  {:name ::find-users-async
   :enter (fn [context]
            (println "AAAA" context)
            (ring-resp/response
              (user-repository/find-users-async)))})

(defn create-user
  [request]
  (user-repository/create-user (:json-params request))
  (ring-resp/response nil))

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/json-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors home-page) :route-name :home]
              ["/users/:user-id" :get (conj common-interceptors find-user) :route-name :get-user]
              ["/users" :post (conj common-interceptors create-user) :route-name :create-user]
              ["/users" :get (conj common-interceptors find-users) :route-name :find-users]
              ["/users-async" :get (conj common-interceptors find-users-async-2) :route-name :find-users-async]
              ["/about" :get (conj common-interceptors about-page) :route-name ::about-page]})

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

;; Terse/Vector-based routes
;(def routes
;  `[[["/" {:get home-page}
;      ^:interceptors [(body-params/body-params) http/html-body]
;      ["/about" {:get about-page}]]]])


