/*
 * Remember to SET ROLE vgowner;
 */
ALTER TABLE 
	stock_email
ADD	
	email_html_body_tx TEXT;
