FROM clojure
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar
EXPOSE 8080
CMD ["java", "-jar", "app-standalone.jar"]