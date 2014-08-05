DROP TABLE IF EXISTS membership_payment;
CREATE TABLE membership_payment
(
	id                 SERIAL PRIMARY KEY,
	member_id          BIGINT NOT NULL,
	amount             NUMERIC(8,2) NOT NULL,
	due_dt             TIMESTAMP,
	received_dt        TIMESTAMP,
	payment_method_cd  VARCHAR(10),
	FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
	FOREIGN KEY (payment_method_cd) REFERENCES payment_method (payment_method_cd)
);