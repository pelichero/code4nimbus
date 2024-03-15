(defproject clojure-simple-http "0.1.0-SNAPSHOT"
  :description "A simple HTTP server"
  :min-lein-version "2.7.1"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "2.5.0"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.slf4j/slf4j-api "1.7.32"]
                 [ch.qos.logback/logback-classic "1.2.6"]
                 [http-kit "2.2.0"]
                 [clj-time "0.14.0"]
                 [metosin/compojure-api "2.0.0-alpha26"]
                 [javax.xml.bind/jaxb-api "2.3.0"]
                 [com.datomic/peer "1.0.7075"]]
  :profiles {:uberjar {:aot [com.code4nimbus.clojureapi.core]}}
  :jvm-opts ["-Dclojure.tools.logging.factory=clojure.tools.logging.impl/slf4j-factory"]
  :main com.code4nimbus.clojureapi.core
  :aot [com.code4nimbus.clojureapi.core])