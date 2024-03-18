# clojure-simple-http

A simple HTTP server in Clojure

## Usage

  1. Clone repo `git clone https://github.com/pelichero/code4nimbus`
  2. `cd clojure-api`
  3. `lein run`
  4. Goto [localhost:9000](http://localhost:9000)

## Building docker

  1. Go to directory which contains `Dockerfile`
  2. run `docker build -t server-app .` to build the image
  3. run `docker run -it --rm -p 8088:8080 server-app` to run the container