# CQL Examples

## Create keyspace and table
```sql
CREATE KEYSPACE cql1 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 2};
USE cql1;

CREATE TABLE nobel_laureates 
(
  year int, 
  category text, 
  laureateid int, 
  firstname text, 
  surname text, 
  borncountrycode text, 
  borncity text, 
  PRIMARY KEY (year, laureateid)
);

COPY nobel_laureates (year, category, laureateid, firstname, surname, borncountrycode, borncity) FROM '/demo/NobelLaureatesLimited.csv';
```

## SELECT statements
```sql
SELECT * FROM nobel_laureates LIMIT 10;

SELECT * FROM nobel_laureates WHERE year = 2010;

SELECT * FROM nobel_laureates WHERE borncountrycode = 'HU';

CREATE INDEX nobel_laureates_borncountrycode ON nobel_laureates (borncountrycode);

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics';

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics' ALLOW FILTERING;
```

## Helpers
```bash
curl http://medvekoma.net/reveal/cassandra/NobelLaureatesLimited.csv > NobelLaureatesLimited.csv

nodetool rebuild_index sep1 nobel_laureates nobel_laureates.nobel_laureates_borncountrycode
```
