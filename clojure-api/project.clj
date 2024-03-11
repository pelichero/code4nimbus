(defproject clojure-simple-http "0.1.0-SNAPSHOT"
  :description "A simple HTTP server"
  :min-lein-version "2.7.1"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [http-kit "2.2.0"]
                 [clj-time "0.14.0"]
                 [metosin/compojure-api "2.0.0-alpha26"]
                 [javax.xml.bind/jaxb-api "2.3.0"]
                 [com.datomic/peer "1.0.7075"]]
  :profiles {:uberjar {:aot [com.code4nimbus.clojureapi.core]}}
  :main com.code4nimbus.clojureapi.core
  :aot [com.code4nimbus.clojureapi.core])