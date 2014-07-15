DROP TABLE IF EXISTS member_telephone;
CREATE TABLE member_telephone
(
	id          serial  PRIMARY KEY,
	member_id   bigint  NOT NULL,
	telephone_number varchar(20) NOT NULL,
	telephone_type_cd varchar(10)  NOT NULL,
	FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
	FOREIGN KEY (telephone_type_cd) REFERENCES telephone_type (telephone_type_cd)
);