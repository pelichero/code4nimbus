# Clojure and Datomic Studies with Docker and Kafka

![Clojure Icon](https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Clojure_logo.svg/256px-Clojure_logo.svg.png?20161016020557)
![Datomic Icon](https://www.datomic.com/images/datomic-logo-290x230.png)
![Docker Icon](https://seeklogo.com/images/D/docker-logo-6D6F987702-seeklogo.com.png)
![Kafka Icon](https://cdn.icon-icons.com/icons2/2699/PNG/512/apache_kafka_logo_icon_167866.png)

Welcome to my monorepo project dedicated to exploring Clojure and Datomic while leveraging Docker and Kafka technologies. This repository documents my study path and progress as I delve into these exciting areas of software development.

## Project Structure

The repository is organized into the following sections:

- `clojure-api`: Contains Web API projects built with Clojure.
- `datomic-datbase`: Includes a sample Datomic database and tools to interact with it.

# How to put thing up!.


- [Storage Services and Transactor](#storage-services-and-transactor)
   - [Dev Mode](#dev-mode)
   - [PostgreSQL](#postgresql)
- [Tools and Utilities](#tools-and-utilities)
   - [Datomic Console](#datomic-console)
   - [REPL](#repl)

## Storage Services and Transactor

Official Documentation: [Storage Services](https://docs.datomic.com/pro/overview/storage.html)

### Dev Mode

Official Documentation: [Provisioning dev mode](https://docs.datomic.com/pro/overview/storage.html#provisioning-dev-mode)

Run the Datomic Transactor:

```sh
docker compose up datomic-transactor
```

To restore a backup of the [MusicBrainz](https://musicbrainz.org) Sample Database:

```sh
docker compose run datomic-tools ./bin/datomic restore-db file:/usr/mbrainz-1968-1973 "datomic:dev://datomic-transactor:4334/my-datomic?password=unsafe"
````

## Web Application

To run the Datomic Peer Server and a web application:

```sh
docker compose up server-app
```

## Tools and Utilities

### Datomic Console

To run [Datomic Console](https://docs.datomic.com/pro/other-tools/console.html):

```sh
docker compose up datomic-console
```

http://localhost:8080/browse

![Screenshot of the Datomic Console interface](https://docs.datomic.com/pro/images/console-window.png "Screenshot of the Datomic Console interface")

### REPL

Starting a REPL:
```sh
docker compose run datomic-tools clojure -M:repl
````

Documentation: [Getting Started](https://docs.datomic.com/pro/getting-started/brief-overview.html)

Require the API and set the appropriate `db-uri`:

```clojure
(require '[datomic.api :as d])

; If you are using Dev Mode:
(def db-uri "datomic:dev://datomic-transactor:4334/my-datomic/?password=unsafe")
```

If you have restored a backup of the [MusicBrainz](https://musicbrainz.org) sample database:
```clj
(def conn (d/connect db-uri))

(def db (d/db conn))

(d/q '[:find ?id ?type ?gender
         :in $ ?name
       :where
         [?e :artist/name ?name]
         [?e :artist/gid ?id]
         [?e :artist/type ?teid]
         [?teid :db/ident ?type]
         [?e :artist/gender ?geid]
         [?geid :db/ident ?gender]]
     db
    "Jimi Hendrix")
```

If you are creating a new database from scratch:

```clj
(d/create-database db-uri)

(def conn (d/connect db-uri))

@(d/transact conn [{:db/doc "Hello world"}])

@(d/transact conn [{:db/ident :movie/title
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The title of the movie"}

                   {:db/ident :movie/genre
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The genre of the movie"}

                   {:db/ident :movie/release-year
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The year the movie was released in theaters"}])

@(d/transact conn [{:movie/title "The Goonies"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Commando"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Repo Man"
                    :movie/genre "punk dystopia"
                    :movie/release-year 1984}])

(def db (d/db conn))

(d/q '[:find ?e ?movie-title
       :where [?e :movie/title ?movie-title]]
     db)
```


To exit the REPL, press Ctrl+D or type:
```clojure
:repl/quit
```

Sometimes the REPL insists on hanging; in that case, you can kill the container:
```sh
docker compose kill datomic-tools
```

Dependencies reference:

- [org.clojure/clojure](https://central.sonatype.com/artifact/org.clojure/clojure/overview)
- [com.datomic/peer](https://central.sonatype.com/artifact/com.datomic/peer/overview)
- [org.postgresql/postgresql](https://central.sonatype.com/artifact/org.postgresql/postgresql)
- [com.bhauman/rebel-readline](https://clojars.org/com.bhauman/rebel-readline)


## Study Path

1. **Getting Started with Clojure**
   - Explore basic syntax and functional programming concepts.
   - Implement small coding exercises to solidify understanding.

2. **Diving into Datomic**
   - Study Datomic's data model and architecture.
   - Set up a sample Datomic database and interact with it from a Clojure application.

3. **Working with Docker**
   - Learn Docker fundamentals and containerization concepts.
   - Create Dockerfiles for Clojure applications and Datomic instances.

4. **Building with Kafka**
   - Understand the principles of event streaming and Kafka.
   - Develop a simple event producer/consumer system using Kafka and Clojure.

5. **Integration and Projects**
   - Combine knowledge gained from previous steps to build more complex applications.
   - Experiment with connecting Clojure applications to Datomic using Dockerized environments.
   - Explore advanced Kafka scenarios in Clojure-based microservices.

## Contributions

Contributions to this repository are welcome! Feel free to submit pull requests if you find any issues, have improvements to suggest, or want to add new study resources related to Clojure, Datomic, Docker, or Kafka.

## Resources

- [Clojure Official Documentation](https://clojure.org/guides/learn)
- [Datomic Documentation](https://docs.datomic.com/)
- [Docker Documentation](https://docs.docker.com/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)

## Acknowledgments

Icons made by [Iconfinder](https://www.iconfinder.com/) and [Freepik](https://www.freepik.com/) from [www.flaticon.com](https://www.flaticon.com/).

