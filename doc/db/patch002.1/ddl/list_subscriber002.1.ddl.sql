DROP TABLE IF EXISTS list_subscriber;
CREATE TABLE list_subscriber
(
	id                 SERIAL PRIMARY KEY,
	contact_list_id    BIGINT NOT NULL,
	title              VARCHAR(20),
	firstname          VARCHAR(50),
	lastname           VARCHAR(50),
	email              VARCHAR(200),
	FOREIGN KEY (contact_list_id) REFERENCES contact_list (id) ON DELETE CASCADE
);
