# CQL Examples

## Special data types

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
  (1, 'Peter Dontcall', {'Home':'+36 1 1234567','Work':'+36 30 7894561'});

SELECT * FROM contacts;

UPDATE Contacts 
SET Phones = Phones + {'Other':'+1 205 7897897'} 
WHERE Id = 1;

select * from contacts;
```
```bash
cassandra-cli
list Contacts;
```

### User defined types

```sql
CREATE TYPE item 
(id int, name text, price decimal);

CREATE TABLE invoice 
(id int PRIMARY KEY, buyer text, items list<frozen<item>>);

INSERT INTO invoice (id, buyer, items) 
VALUES (1, 'Joe', [{id: 1, name: 'Book', price: 11.99}, {id: 2, name: 'Computer', price: 3333}]);

SELECT * FROM invoice;

UPDATE invoice 
SET items = items + [{id: 3, name: 'Cable', price: 1.00}] 
WHERE id = 1;
```

## Filtering

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
  PRIMARY KEY ((year), laureateid)
);

COPY nobel_laureates 
(year, category, laureateid, firstname, surname, borncountrycode, borncity) 
FROM '/demo/nobel-laureates.csv';

SELECT * FROM nobel_laureates LIMIT 10;

SELECT * FROM nobel_laureates WHERE year = 2010;

SELECT * FROM nobel_laureates WHERE borncountrycode = 'HU';

CREATE INDEX ON nobel_laureates (borncountrycode);

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics';

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics' ALLOW FILTERING;
```

## Inserting and Updating

```sql
CREATE TABLE user
(
  id int,
  firstname text,
  lastname text,
  PRIMARY KEY (id)
);

INSERT INTO user (id, firstname, lastname) 
VALUES ( 1, 'Ada', 'Lovelace');

INSERT INTO user (id, firstname, lastname) 
VALUES ( 1, 'Charles', 'Babbage');

SELECT * FROM user;

UPDATE user
SET firstname='Linus', lastname='Torvalds'
WHERE id=2;
```

## Primary key

### Partition key
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
### Clustering key

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

## Ordering

```sql
USE nobel;

DESCRIBE TABLE nobel_laureates;

SELECT * FROM nobel_laureates WHERE year=2002;

SELECT * FROM nobel_laureates WHERE year=2002 
ORDER BY laureateid DESC;

SELECT * FROM nobel_laureates WHERE year=2002
ORDER BY borncountrycode;

SELECT * FROM nobel_laureates WHERE borncountrycode='HU';

SELECT * FROM nobel_laureates WHERE borncountrycode='HU'
ORDER BY laureateid;

SELECT * FROM nobel_laureates WHERE year in (2002, 1992)
ORDER BY laureateid;
```

## Static fields
```sql
CREATE TABLE Bill_Static
(
	BillId int,
	Seller text static,
	Buyer text static,
	ItemId int,
	ItemName text,
	ItemPrice decimal,
	PRIMARY KEY (BillId, ItemId)
);

INSERT INTO Bill_Static 
(
	billid, Seller, Buyer, 
	itemid, ItemName, ItemPrice
) 
VALUES 
( 
	1, 'Seller', 'Buyer', 
	1001, 'iPad 32GB', 1299.00
);

INSERT INTO Bill_Static 
(
	billid, 
	itemid, ItemName, ItemPrice
) 
VALUES 
( 
	1, 
	1015, 'Headset XDF', 79.90
);

SELECT * FROM Bill_Static;
```
Check internal representation
```bash
cassandra-cli
list Bill_Static;
```