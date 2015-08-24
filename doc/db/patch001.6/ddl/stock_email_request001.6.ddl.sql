/*
 * Remember to SET ROLE vgowner;
 */
ALTER TABLE 
	stock_email_request
ADD	
	recipient_email_tx TEXT;
	
ALTER TABLE
	stock_email_request
ALTER COLUMN
	member_id 
DROP NOT NULL;
