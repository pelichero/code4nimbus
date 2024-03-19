# Datomic Database

- [Storage Services and Transactor](#storage-services-and-transactor)
    - [Dev Mode](#dev-mode)
- [Web Application](#web-application)
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

## Tools and Utilities

### Datomic Console

To run [Datomic Console](https://docs.datomic.com/pro/other-tools/console.html):

```sh
docker compose up datomic-console
```

http://localhost:9090/browse

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
