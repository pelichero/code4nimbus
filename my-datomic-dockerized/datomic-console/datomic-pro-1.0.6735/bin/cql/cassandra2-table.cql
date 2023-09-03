-- DROP TABLE IF EXISTS datomic2.datomic2;

CREATE TABLE IF NOT EXISTS datomic2.datomic2
(
  id2 text PRIMARY KEY,
  rev bigint,
  map text,
  val blob,
  chunks int
);
