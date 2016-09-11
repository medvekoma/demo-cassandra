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
CREATE TYPE Item 
(Id int, Name text, Price decimal);

CREATE TABLE Invoice 
(Id int primary key, Buyer text, items list<frozen<item>>);

insert into invoice (id, buyer, items) 
values (1, 'Joe', [{id: 1, name: 'Book', price: 11.99}, {id: 2, Name: 'Computer', Price: 3333}]);

select * from invoice;

update invoice 
set items = items + [{id: 3, name: 'Cable', price: 1.00}] 
where id = 1;
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

COPY nobel_laureates (year, category, laureateid, firstname, surname, borncountrycode, borncity) 
FROM '/demo/nobel-laureates.csv';

SELECT * FROM nobel_laureates LIMIT 10;

SELECT * FROM nobel_laureates WHERE year = 2010;

SELECT * FROM nobel_laureates WHERE borncountrycode = 'HU';

CREATE INDEX nobel_laureates_borncountrycode ON nobel_laureates (borncountrycode);

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics';

SELECT * FROM nobel_laureates WHERE borncountrycode='RU' AND category='physics' ALLOW FILTERING;
```

## Inserting and Updating

TBD

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

TBD

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