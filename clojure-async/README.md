# clojure-simple-http

A simple HTTP server in Clojure

## Usage

1. Clone repo `git clone https://github.com/pelichero/code4nimbus`
2. `cd clojure-async`
3. `lein run`
4. Goto [http://localhost:9001/swagger](http://localhost:9001/swagger)

## Building docker

1. Go to directory which contains `Dockerfile`
2. run `docker build -t clojure-async .` to build the image
3. run `docker run -it --rm -p 9001:9001 clojure-async` to run the container