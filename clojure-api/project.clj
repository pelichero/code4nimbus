(defproject clojure-api "0.1.0-SNAPSHOT"
  :description "A simple HTTP server"
  :min-lein-version "2.7.1"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[environ "1.1.0"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "2.5.0"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.6"]
                 [org.apache.kafka/kafka-clients "3.6.1"]
                 [org.apache.kafka/kafka_2.12 "3.6.1"]
                 [http-kit "2.2.0"]
                 [clj-time "0.14.0"]
                 [ring/ring-defaults "0.3.1"]
                 [metosin/compojure-api "2.0.0-alpha26"]
                 [javax.xml.bind/jaxb-api "2.3.0"]
                 [com.datomic/peer "1.0.7075"]
                 [io.prometheus/simpleclient_hotspot "0.1.0"]
                 [eigenhombre/namejen "0.1.23"]
                 [iapetos "0.1.7"]]
  :profiles {:uberjar {:aot [com.code4nimbus.clojureapi.core]}}
  :resource-paths ["resources"]
  :main com.code4nimbus.clojureapi.core
  :aot [com.code4nimbus.clojureapi.core])