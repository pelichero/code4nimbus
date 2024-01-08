-- DROP KEYSPACE IF EXISTS datomic2;

CREATE KEYSPACE IF NOT EXISTS datomic2 WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};
