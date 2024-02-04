(defproject clojure-simple-http "0.1.0-SNAPSHOT"
  :description "A simple HTTP server"
  :min-lein-version "2.7.1"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [http-kit "2.2.0"]
                 [clj-time "0.14.0"]
                 [compojure "1.6.0"]
                 [javax.xml.bind/jaxb-api "2.3.0"]
                 [com.datomic/datomic-free "0.9.5697"]]
  :profiles {:uberjar {:aot [http.core]}}
  :main http.core
  :aot [http.core])