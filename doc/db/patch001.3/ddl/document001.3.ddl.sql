DROP TABLE document;
CREATE TABLE document
(
	id              	serial PRIMARY KEY,
    creation_ts         timestamp not null,
	filename_tx         varchar(100) not null,
	description_tx      varchar(100) not null,
	CONSTRAINT filename_uk UNIQUE(filename_tx)
);
