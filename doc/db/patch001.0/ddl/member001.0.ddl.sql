DROP TABLE member;
CREATE TABLE member
(
	memberno        bigint          PRIMARY KEY,
	title           varchar(20),
	firstname       varchar(30),
	surname         varchar(30),	
	organisation    varchar(50),	
	addressline1    varchar(100),
	addressline2    varchar(100),
	addressline3    varchar(100),
	addressline4    varchar(100),
	postcode        varchar(10),
	email           varchar(200),
	dob             varchar(10),
	seis	        boolean,
	rollcall        boolean,
	certificateGenerated timestamp,
	certificateSent timestamp,
	totalinvestment numeric(8,2)    not null
);
