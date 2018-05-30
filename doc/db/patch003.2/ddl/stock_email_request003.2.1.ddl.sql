ALTER TABLE stock_email_request
	ADD list_subscriber_id bigint;

ALTER TABLE stock_email_request
	ADD FOREIGN KEY (list_subscriber_id) REFERENCES list_subscriber(id);

