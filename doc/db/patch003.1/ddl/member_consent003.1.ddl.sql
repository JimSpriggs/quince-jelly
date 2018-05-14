DROP TABLE member_consent;
CREATE TABLE member_consent
(
	id              	serial PRIMARY KEY,
	member_id         bigint  NOT NULL,
	marketing_in      boolean,
  creation_ts       timestamp not null,
  update_ts         timestamp not null,
	FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);
