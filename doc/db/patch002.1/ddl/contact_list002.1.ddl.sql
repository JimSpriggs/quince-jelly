DROP TABLE contact_list;
CREATE TABLE contact_list
(
	id              	serial PRIMARY KEY,
	description_tx      varchar(100) not null,
  creation_ts         timestamp not null,
	CONSTRAINT description_uk UNIQUE(description_tx)
);
