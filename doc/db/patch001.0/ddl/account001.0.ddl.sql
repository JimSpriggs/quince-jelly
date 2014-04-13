/*
 * Remember to SET ROLE vgowner;
 */
DROP TABLE account;
CREATE TABLE account
(
	id       serial                 PRIMARY KEY,
	email    varchar(200)           not null,
	password varchar(200)           not null,
	CONSTRAINT email_uk             UNIQUE(email)
);
