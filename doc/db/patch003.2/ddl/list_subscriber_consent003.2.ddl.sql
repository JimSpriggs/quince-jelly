DROP TABLE list_subscriber_consent;
CREATE TABLE list_subscriber_consent
(
	id              	  serial PRIMARY KEY,
	list_subscriber_id  bigint  NOT NULL,
	marketing_in        boolean,
  creation_ts         timestamp not null,
  update_ts           timestamp not null,
	FOREIGN KEY (list_subscriber_id) REFERENCES list_subscriber (id) ON DELETE CASCADE
);
