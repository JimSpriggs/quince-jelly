/*
 * Remember to SET ROLE vgowner;
 */
ALTER TABLE 
	stock_email
ADD	
	email_html_body_tx TEXT;

ALTER TABLE
	stock_email
ADD
	creation_ts TIMESTAMP;
	