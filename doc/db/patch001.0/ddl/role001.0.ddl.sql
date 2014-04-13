DROP TABLE IF EXISTS role;
CREATE TABLE role
(
	id       serial                 PRIMARY KEY,
	name     varchar(20)           	NOT NULL,
	description varchar(100)		NOT NULL
);