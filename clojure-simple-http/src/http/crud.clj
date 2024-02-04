(ns http.crud
  (:require [datomic.api :as d]))

;; new database
(def uri "datomic:dev://localhost:4334/sample-db")
(d/create-database uri)
(def conn (d/connect uri))

;; attribute schema for :crud/name
@(d/transact
   conn
   [{:db/id (d/tempid :db.part/db)
     :db/ident :crud/name
     :db/valueType :db.type/string
     :db/unique :db.unique/identity
     :db/cardinality :db.cardinality/one
     :db.install/_attribute :db.part/db}])

;; create, awaiting point-in-time-value
(def db-after-create
  (-> (d/transact
        conn
        [[:db/add (d/tempid :db.part/user) :crud/name "Hello world"]])
      deref
      :db-after))

;; read
(d/pull db-after-create '[*] [:crud/name "Hello world"])

;; update
(-> (d/transact
      conn
      [[:db/add [:crud/name "Hello world"]
        :db/doc "An entity with only demonstration value"]])
    deref
    :db-after
    (d/pull '[*] [:crud/name "Hello world"]))

;; "delete" adds new information, does not erase old
(def db-after-delete
  (-> (d/transact conn
                  [[:db.fn/retractEntity [:crud/name "Hello world"]]])
      deref
      :db-after))

;; no present value for deleted entity
(d/pull db-after-delete '[*] [:crud/name "Hello world"])

;; but everything ever said is still there
(def history (d/history db-after-delete))
(require '[clojure.pprint :as pp])
(->> (d/q '[:find ?e ?a ?v ?tx ?op
            :in $
            :where [?e :crud/name "Hello world"]
            [?e ?a ?v ?tx ?op]]
          history)
     (map #(zipmap [:e :a :v :tx :op] %))
     (sort-by :tx)
     (pp/print-table [:e :a :v :tx :op]))
