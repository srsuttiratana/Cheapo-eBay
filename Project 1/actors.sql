CREATE TABLE Actors(Name VARCHAR(40), Movie VARCHAR(80), Year INTEGER, Role VARCHAR(40))ENGINE = INNODB;

LOAD DATA LOCAL INFILE '~/data/actors.csv' INTO TABLE Actors
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

SELECT Name as 'Actors in Die Another Day' FROM Actors WHERE Movie = 'Die Another Day';

SELECT DISTINCT Movie as 'Movies in this Database' FROM Actors;

SELECT Name as 'Actors in Harry Potter and the Chamber of Secrets' FROM Actors WHERE Movie = 'Harry Potter and the Chamber of Secrets';

DROP TABLE Actors;