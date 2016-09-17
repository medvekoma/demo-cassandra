# Samples

## One

```sql
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

CREATE INDEX ON nobel_laureates (borncountrycode);

SELECT * FROM nobel_laureates WHERE year = 2010;

UPDATE nobel_laureates
SET borncountrycode = 'HU'
WHERE laureateid = 9999;
```

## Two

```sql
CREATE TYPE article 
(
  id int, 
  name text, 
  price decimal
);

CREATE TABLE invoice 
(
  id int PRIMARY KEY, 
  buyer text, 
  items list<frozen<article>>
);
```

## Three

```sql
CREATE TABLE car_registry_1
(
  regnr text,
  car text,
  owner text,
  PRIMARY KEY (regnr)
);

-- Don't do this in production :)
CREATE TABLE car_registry_2
(
  country text,
  regnr text,
  car text,
  owner text,
  PRIMARY KEY (country, regnr)
);

-- Don't do this in production :)
CREATE TABLE car_registry_3
(
  country text,
  cname text static,
  regnr text,
  car text,
  owner text,
  PRIMARY KEY (country, regnr)
);
```
## Upserts

```sql
CREATE TABLE user
(
  id int PRIMARY KEY,
  firstname text,
  lastname text,
);

INSERT INTO user (id, firstname, lastname) 
VALUES ( 1, 'Ada', 'Lovelace');

-- Q: Key violation?
INSERT INTO user (id, firstname, lastname) 
VALUES ( 1, 'Charles', 'Babbage');

-- Nothing to update...
UPDATE user
SET firstname='Linus', lastname='Torvalds'
WHERE id=2;

-- ~ equivalent to...
INSERT INTO user (id, firstname, lastname)
VALUES (2, 'Linus', 'Torwalds');
```

### Filtering by Key

```sql
[...]
PRIMARY KEY (pk, ck1, ck2)

-- partition key fields must be specified exactly
SELECT * FROM table1 WHERE pk = value;

-- IN operator is supported 
--   (caution, can overload the cluster)
SELECT * FROM table1 WHERE pk IN (v1, v2, v3);

-- Can use unequality operators for clustering columns
SELECT * FROM table1 WHERE pk = v1
AND ck1 >= v2

-- INCORRECT 
-- must restrict by preceding columns first
SELECT * FROM table1 WHERE pk = v1
AND ck2 = v2

-- INCORRECT
-- must restrict preceding columns by equality
SELECT * FROM table1 WHERE pk = v1
AND ck1 >= v2 AND ck2 <= v3

-- Slicing. can combine clustering columns
SELECT * FROM table1 WHERE pk = v1
AND (ck1, ck2) >= (v1, v2)
```

## Secondary indexes

```sql
CREATE TABLE track (
  track_id int,
  title text,
  album text,
  artist text,
  PRIMARY KEY(album, track_id)
);

-- ERROR! 
-- Cannot filter on non-key, non-indexed fields
SELECT * FROM track WHERE artist = 'U2';

CREATE INDEX ON track(artist);

-- OK. filter by secondary index
SELECT * FROM track WHERE artist = 'U2';

SELECT * FROM track 
WHERE album = 'Joshua Tree'
AND artist = 'U2';
```

## Ordering

```sql
-- Can change the default ASC ordering of clustering key
CREATE TABLE transaction (
  accountnr text,
  transactiondate timestamp,
  recipient text,
  amount decimal,
  PRIMARY KEY (accountnr, transactiondate)
) WITH CLUSTERING ORDER BY (transactiondate DESC);

-- Records are sorted as specified in the table definition
SELECT * FROM transaction WHERE accountnr = '001';

-- Records are sorted in ASC order 
-- (slower if the clustering order doesn't match)
SELECT * FROM transaction WHERE accountnr = '001'
ORDER BY transactiondate;

-- Records are sorted in DESC order 
SELECT * FROM transaction WHERE accountnr = '001'
ORDER BY transactiondate DESC;
```

## Deletions

```sql
-- Delete a row
DELETE FROM table1 WHERE pk = value;

-- Time to live feature (seconds)
-- Row is deleted on expiry
INSERT INTO table1 (pk, f1)
VALUES (v1, v2)
USING TTL 60;
```
