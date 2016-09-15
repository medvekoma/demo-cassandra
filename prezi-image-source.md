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
