DROP TABLE IF EXISTS account_role;
CREATE TABLE account_role
(
	id          serial  PRIMARY KEY,
	account_id  bigint  NOT NULL,
	role_id     bigint  NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
	FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);