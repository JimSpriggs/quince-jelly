DROP TABLE IF EXISTS membership_payment;
CREATE TABLE membership_payment
(
	id serial  PRIMARY KEY,
	member_id   bigint NOT NULL,
	amount      numeric(8,2) NOT NULL,
	paymentdate timestamp NOT NULL,
	FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
);