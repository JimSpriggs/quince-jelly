DROP TABLE IF EXISTS member_status;
CREATE TABLE member_status
(
	member_status_cd varchar(20) PRIMARY KEY,
	description      varchar(100) NOT NULL
);