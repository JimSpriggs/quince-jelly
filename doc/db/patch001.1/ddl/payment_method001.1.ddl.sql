DROP TABLE IF EXISTS payment_method;
CREATE TABLE payment_method
(
	payment_method_cd varchar(10) PRIMARY KEY,
	description    varchar(100) NOT NULL
);