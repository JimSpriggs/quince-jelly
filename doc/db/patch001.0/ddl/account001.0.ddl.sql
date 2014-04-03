DROP TABLE account;
CREATE TABLE account
(
	id       bigint                 PRIMARY KEY,
	email    varchar(200)           not null,
	password varchar(200)           not null,
	role     varchar(20)            not null,
	CONSTRAINT email_uk             UNIQUE(email)
);
