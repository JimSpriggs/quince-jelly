/*
 * Remember to SET ROLE vgowner;
 */
DROP TABLE account;
CREATE TABLE account
(
	id              serial               PRIMARY KEY,
	firstName       varchar(20)			 not null,
	surname         varchar(30)			 not null,
	email           varchar(200)         not null,
	password        varchar(200)         not null,
	creation_dt	    timestamp			 not null,
	active          boolean				 not null,
	activation_dt   boolean,
	CONSTRAINT email_uk              UNIQUE(email)
);
