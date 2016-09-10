# Cassandra write path by example

## Create test table with initial data
```sql
CREATE KEYSPACE demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE demo;
CREATE TABLE meetup
(
  id int PRIMARY KEY,
  presenter text,
  topic text,
  date timestamp
);
INSERT INTO meetup (id, presenter, topic) VALUES ( 1, 'Ada Lovelace', 'Storing the Internet on a Raspberry Pi');
SELECT * FROM meetup;
```

## Check the data files on the node (sstable)
```bash
# folder for the sstables
cd /var/lib/cassandra/data/demo/meetup-<GUID>/
ls -la
# no data files yet

# flush the memtable to disk
nodetool flush

# check the content of the sstable
java -jar /demo/sstable-tools.jar toJson ma-1-big-Data.db
```

## Update data
```sql
# enter CQL shell to modify the data
cqlsh
-- update some columns
UPDATE demo.meetup
SET date = '2016-06-16', presenter = 'Charles Babbage'
WHERE id = 1;
-- verify results
SELECT * FROM demo.meetup;
```

```bash
ls -la
java -jar /demo/sstable-tools.jar toJson ma-1-big-Data.db
# no new data files - new data is still in memory

# flush data to disk
nodetool flush

# check sstable content again
ls -la
java -jar /demo/sstable-tools.jar toJson ma-1-big-Data.db
java -jar /demo/sstable-tools.jar toJson ma-2-big-Data.db

# compact data files
nodetool compact

# check sstable content again
ls -la
java -jar /demo/sstable-tools.jar toJson ma-3-big-Data.db
```
