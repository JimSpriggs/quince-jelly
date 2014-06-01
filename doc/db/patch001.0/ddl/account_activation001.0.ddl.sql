DROP TABLE IF EXISTS account_activation;
CREATE TABLE account_activation
(
	id serial  PRIMARY KEY,
	account_id  bigint  NOT NULL,
	creation_dt TIMESTAMP NOT NULL,
	access_key varchar(40) NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    CONSTRAINT activation_access_key_uk UNIQUE(access_key)
);