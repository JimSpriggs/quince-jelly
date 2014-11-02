/*
 * Remember to SET ROLE vgowner;
 */
DROP TABLE stock_email;
CREATE TABLE stock_email
(
	id              	serial               PRIMARY KEY,
	email_purpose_tx    varchar(20) not null,
	email_body_tx       text not null,
	email_subject_tx    varchar(200) not null,
	CONSTRAINT email_purpose_uk UNIQUE(email_purpose_tx)
);
