DROP TABLE IF EXISTS stock_email_attachment;
CREATE TABLE stock_email_attachment
(
	id                 SERIAL PRIMARY KEY,
	stock_email_id      BIGINT NOT NULL,
	document_id         BIGINT NOT NULL,
	FOREIGN KEY (stock_email_id) REFERENCES stock_email (id) ON DELETE CASCADE,
	FOREIGN KEY (document_id) REFERENCES document (id) ON DELETE CASCADE
);
