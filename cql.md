# CQL Examples

## Primary key

### Primary key is partition key
```sql
CREATE TABLE User1
(
  UserId int,
  FirstName text,
  LastName text,
  PRIMARY KEY (UserId)
);

INSERT INTO user1 (userid, firstname, lastname) VALUES ( 1, 'Ada', 'Lovelace');
INSERT INTO user1 (userid, firstname, lastname) VALUES ( 2, 'Charles', 'Babbage');
select * from user1;
```
```bash
cassandra-cli
list User1;
```
### Primary key is partition key + clustering key

```sql
CREATE TABLE User2 
(
  userid int, 
  countrycode text, 
  firstname text, 
  lastname text, 
  primary key ((userId), CountryCode)
);

INSERT INTO User2 (userid, CountryCode, firstname, lastname) VALUES ( 1, 'UK', 'Ada', 'Lovelace');
INSERT INTO User2 (userid, CountryCode, firstname, lastname) VALUES ( 2, 'UK', 'Charles', 'Babbage');
INSERT INTO User2 (userid, CountryCode, firstname, lastname) VALUES ( 1, 'HU', 'John', 'von Neumann');

select * from user2;
```
```bash
cassandra-cli
list User2;
```

### Collections
```sql
CREATE TABLE Contacts 
(
  Id int, 
  Name text, 
  Phones map<text, text>, 
  PRIMARY KEY (Id)
);

INSERT INTO Contacts 
  (Id, Name, Phones) 
VALUES 
  ( 1, 'Peter Dontcall', {'Home':'+36 1 1234567','Work':'+36 30 7894561'});

SELECT * FROM contacts;

UPDATE 
  Contacts 
SET 
  Phones = Phones + {'Other':'+1 205 7897897'} 
WHERE 
  Id = 1;

select * from contacts;
```
```bash
cassandra-cli
list Contacts;
```

## Queries
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
  PRIMARY KEY ((year), laureateid)
);

COPY nobel_laureates (year, category, laureateid, firstname, surname, borncountrycode, borncity) 
FROM '/demo/nobel-laureates.csv';
```

### SELECT statements
```sql
SELECT * FROM nobel_laureates LIMIT 10;

SELECT * FROM nobel_laureates WHERE year = 2010;

SELECT * FROM nobel_laureates WHERE borncountrycode = 'HU';

CREATE INDEX nobel_laureates_borncountrycode ON nobel_laureates (borncountrycode);

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics';

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics' ALLOW FILTERING;
```

### Helpers
```bash
curl http://medvekoma.net/reveal/cassandra/NobelLaureatesLimited.csv > NobelLaureatesLimited.csv

nodetool rebuild_index sep1 nobel_laureates nobel_laureates.nobel_laureates_borncountrycode
```
