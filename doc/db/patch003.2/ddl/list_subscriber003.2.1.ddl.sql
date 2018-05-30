ALTER TABLE list_subscriber
	ADD uuid varchar(36),
	ADD CONSTRAINT ls_uuid_uk UNIQUE(uuid);

