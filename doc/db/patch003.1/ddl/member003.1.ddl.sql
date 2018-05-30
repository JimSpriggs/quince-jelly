ALTER TABLE member
	ADD uuid varchar(36),
	ADD CONSTRAINT uuid_uk UNIQUE(uuid);

