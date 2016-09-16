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
