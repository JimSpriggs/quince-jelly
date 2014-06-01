DROP TABLE IF EXISTS password_reset;
CREATE TABLE password_reset
(
	id serial  PRIMARY KEY,
	account_id  bigint  NOT NULL,
	creation_dt TIMESTAMP NOT NULL,
	access_key varchar(40) NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    CONSTRAINT password_reset_access_key_uk UNIQUE(access_key)
);