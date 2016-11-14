SET ROLE vgowner;

CREATE TABLE account
(
    id INTEGER PRIMARY KEY NOT NULL,
    firstname VARCHAR(20) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL,
    creation_dt TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    activation_dt TIMESTAMP
);
CREATE TABLE account_activation
(
    id INTEGER PRIMARY KEY NOT NULL,
    account_id BIGINT NOT NULL,
    creation_dt TIMESTAMP NOT NULL,
    access_key VARCHAR(40) NOT NULL
);
CREATE TABLE account_role
(
    id INTEGER PRIMARY KEY NOT NULL,
    account_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
);
CREATE TABLE document
(
    id INTEGER PRIMARY KEY NOT NULL,
    creation_ts TIMESTAMP NOT NULL,
    filename_tx VARCHAR(100) NOT NULL,
    description_tx VARCHAR(100) NOT NULL
);
CREATE TABLE member
(
    id INTEGER PRIMARY KEY NOT NULL,
    title VARCHAR(20),
    firstname VARCHAR(30),
    surname VARCHAR(30),
    organisation VARCHAR(50),
    addressline1 VARCHAR(100),
    addressline2 VARCHAR(100),
    addressline3 VARCHAR(100),
    addressline4 VARCHAR(100),
    postcode VARCHAR(10),
    email VARCHAR(200),
    dob VARCHAR(10),
    seis BOOLEAN,
    rollcall BOOLEAN,
    certificategenerated TIMESTAMP,
    certificatesent TIMESTAMP,
    totalinvestment NUMERIC(8,2) NOT NULL,
    memberno INTEGER,
    member_status_cd VARCHAR(20) NOT NULL,
    committee BOOLEAN DEFAULT false
);
CREATE TABLE member_status
(
    member_status_cd VARCHAR(20) PRIMARY KEY NOT NULL,
    description VARCHAR(100) NOT NULL
);
CREATE TABLE member_summary_20150824
(
    memberno INTEGER,
    title VARCHAR(20),
    firstname VARCHAR(30),
    surname VARCHAR(30),
    organisation VARCHAR(50),
    email VARCHAR(200),
    investment NUMERIC(8,2),
    amount_paid NUMERIC,
    status VARCHAR(20)
);
CREATE TABLE member_telephone
(
    id INTEGER PRIMARY KEY NOT NULL,
    member_id BIGINT NOT NULL,
    telephone_number VARCHAR(20) NOT NULL,
    telephone_type_cd VARCHAR(10) NOT NULL
);
CREATE TABLE membership_payment
(
    id INTEGER PRIMARY KEY NOT NULL,
    member_id BIGINT NOT NULL,
    amount NUMERIC(8,2) NOT NULL,
    due_dt TIMESTAMP,
    received_dt TIMESTAMP,
    payment_method_cd VARCHAR(10)
);
CREATE TABLE password_reset
(
    id INTEGER PRIMARY KEY NOT NULL,
    account_id BIGINT NOT NULL,
    creation_dt TIMESTAMP NOT NULL,
    access_key VARCHAR(40) NOT NULL
);
CREATE TABLE payment_method
(
    payment_method_cd VARCHAR(10) PRIMARY KEY NOT NULL,
    description VARCHAR(100) NOT NULL
);
CREATE TABLE role
(
    id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100) NOT NULL
);
CREATE TABLE stock_email
(
    id INTEGER PRIMARY KEY NOT NULL,
    email_purpose_tx VARCHAR(20) NOT NULL,
    email_body_tx TEXT NOT NULL,
    email_subject_tx VARCHAR(200) NOT NULL,
    email_html_body_tx TEXT,
    creation_ts TIMESTAMP
);
CREATE TABLE stock_email_attachment
(
    id INTEGER PRIMARY KEY NOT NULL,
    stock_email_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL
);
CREATE TABLE stock_email_request
(
    id INTEGER PRIMARY KEY NOT NULL,
    stock_email_id BIGINT NOT NULL,
    member_id BIGINT,
    request_ts TIMESTAMP NOT NULL,
    sent_ts TIMESTAMP,
    error_tx TEXT,
    recipient_email_tx TEXT
);
CREATE TABLE telephone_type
(
    telephone_type_cd VARCHAR(10) PRIMARY KEY NOT NULL,
    description VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX email_uk ON account (email);
ALTER TABLE account_activation ADD FOREIGN KEY (account_id) REFERENCES account (id);
CREATE UNIQUE INDEX activation_access_key_uk ON account_activation (access_key);
ALTER TABLE account_role ADD FOREIGN KEY (account_id) REFERENCES account (id);
ALTER TABLE account_role ADD FOREIGN KEY (role_id) REFERENCES role (id);
CREATE UNIQUE INDEX filename_uk ON document (filename_tx);
ALTER TABLE member ADD FOREIGN KEY (member_status_cd) REFERENCES member_status (member_status_cd);
ALTER TABLE member_telephone ADD FOREIGN KEY (member_id) REFERENCES member (id);
ALTER TABLE member_telephone ADD FOREIGN KEY (telephone_type_cd) REFERENCES telephone_type (telephone_type_cd);
ALTER TABLE membership_payment ADD FOREIGN KEY (member_id) REFERENCES member (id);
ALTER TABLE membership_payment ADD FOREIGN KEY (payment_method_cd) REFERENCES payment_method (payment_method_cd);
ALTER TABLE password_reset ADD FOREIGN KEY (account_id) REFERENCES account (id);
CREATE UNIQUE INDEX password_reset_access_key_uk ON password_reset (access_key);
CREATE UNIQUE INDEX email_purpose_uk ON stock_email (email_purpose_tx);
ALTER TABLE stock_email_attachment ADD FOREIGN KEY (stock_email_id) REFERENCES stock_email (id);
ALTER TABLE stock_email_attachment ADD FOREIGN KEY (document_id) REFERENCES document (id);
ALTER TABLE stock_email_request ADD FOREIGN KEY (stock_email_id) REFERENCES stock_email (id);
ALTER TABLE stock_email_request ADD FOREIGN KEY (member_id) REFERENCES member (id);


-- grant001.0
GRANT ALL ON account TO vguser;
GRANT USAGE ON account_id_seq TO vguser;
GRANT ALL ON member TO vguser;
GRANT USAGE ON member_id_seq TO vguser;
GRANT ALL ON account_role TO vguser;
GRANT USAGE ON account_role_id_seq TO vguser;
GRANT SELECT ON role TO vguser;
GRANT USAGE ON role_id_seq TO vguser;
GRANT ALL ON account_activation TO vguser;
GRANT USAGE ON account_activation_id_seq TO vguser;
GRANT ALL ON password_reset TO vguser;
GRANT USAGE ON password_reset_id_seq TO vguser;

-- grant001.1
GRANT USAGE ON member_no_seq TO vguser;
GRANT ALL ON member_status TO vguser;
GRANT ALL ON telephone_type TO vguser;
GRANT ALL ON payment_method TO vguser;
GRANT ALL ON membership_payment TO vguser;
GRANT USAGE ON membership_payment_id_seq TO vguser;
GRANT ALL ON member_telephone TO vguser;
GRANT USAGE ON member_telephone_id_seq TO vguser;

-- grant001.2
GRANT ALL ON stock_email TO vguser;
GRANT USAGE ON stock_email_id_seq TO vguser;
GRANT ALL ON stock_email_request TO vguser;
GRANT USAGE ON stock_email_request_id_seq TO vguser;

-- grant001.3
GRANT ALL ON document TO vguser;
GRANT USAGE ON document_id_seq TO vguser;
GRANT ALL ON stock_email_attachment TO vguser;
GRANT USAGE ON stock_email_attachment_id_seq TO vguser;

SELECT 'ALTER TABLE ' || quote_ident(MIN(schema_name)) || '.'|| quote_ident(MIN(TABLE_NAME)) || ' ALTER COLUMN ' || quote_ident(MIN(column_name))
|| ' SET DEFAULT NEXTVAL (''' || quote_ident(MIN(seq_name)) || ''');'
FROM (
    SELECT
        n.nspname AS schema_name,
        c.relname AS TABLE_NAME,
        a.attname AS column_name,
        SUBSTRING(d.adsrc FROM E'^nextval\\(''([^'']*)''(?:::text|::regclass)?\\)') AS seq_name
    FROM pg_class c
    JOIN pg_attribute a ON (c.oid=a.attrelid)
    JOIN pg_attrdef d ON (a.attrelid=d.adrelid AND a.attnum=d.adnum)
    JOIN pg_namespace n ON (c.relnamespace=n.oid)
    WHERE has_schema_privilege(n.oid,'USAGE')
      AND n.nspname NOT LIKE 'pg!_%' escape '!'
      AND has_table_privilege(c.oid,'SELECT')
      AND (NOT a.attisdropped)
      AND d.adsrc ~ '^nextval'
) seq
GROUP BY seq_name HAVING COUNT(*)=1;