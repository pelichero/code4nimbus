FROM java:8-alpine
MAINTAINER Mate Magyari <mate.magyari@gmail.com>

ADD target/clojure-pedestal-example-0.0.1-SNAPSHOT-standalone.jar /clojure-pedestal-example/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/clojure-pedestal-example/app.jar"]
