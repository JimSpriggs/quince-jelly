-- set creation_ts on those stock emails which have been sent out
UPDATE stock_email se SET creation_ts = (SELECT MIN(request_ts) FROM stock_email_request WHERE stock_email_id = se.id);

-- for the member certificate email, set the date according to when the first cert was generated
UPDATE stock_email se SET creation_ts = (SELECT MIN(certificategenerated) FROM member) WHERE email_purpose_tx = 'MEMBER_CERTIFICATE';