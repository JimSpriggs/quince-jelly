/*
 * Remember to SET ROLE vgowner;
 */
DROP TABLE stock_email_request;
CREATE TABLE stock_email_request
(
	id              	serial               PRIMARY KEY,
	stock_email_id      BIGINT NOT NULL,
	member_id           BIGINT NOT NULL,
	request_ts          TIMESTAMP NOT NULL,
	sent_ts             TIMESTAMP,
	error_tx            text,
	FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
	FOREIGN KEY (stock_email_id) REFERENCES stock_email (id) ON DELETE CASCADE
);
