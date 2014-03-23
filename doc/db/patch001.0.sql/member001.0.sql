DROP TABLE member;
CREATE TABLE member
(
	memberno        bigint          PRIMARY KEY,
	title           varchar(20)     not null,
	firstname       varchar(30)     not null,
	surname         varchar(30)     not null,	
	addressline1    varchar(100)    not null,
	addressline2    varchar(100),
	addressline3    varchar(100),
	addressline4    varchar(100),
	postcode        varchar(10)     not null,
	email           varchar(200),
	dob             varchar(10)     not null,
	rollcall        boolean         not null,
	totalinvestment numeric(8,2)    not null
);

INSERT INTO member (memberno, title, firstname, surname, addressline1, addressline2, addressline3, addressline4, postcode, email, dob, rollcall, totalinvestment)
VALUES (1, 'Ms', 'Denise', 'McAvoy', '76 Clifton Road', 'Prestwich', 'Manchester', null, 'M25 3HR', 'denise@village-greens-coop.co.uk', '19/05/1970', '1', 10000.00);
INSERT INTO member (memberno, title, firstname, surname, addressline1, addressline2, addressline3, addressline4, postcode, email, dob, rollcall, totalinvestment)
VALUES (2, 'Mr', 'John', 'Hurst', '76 Clifton Road', 'Prestwich', 'Manchester', null, 'M25 3HR', 'jhurst1970@gmail.com', '18/07/1970', '1', 500.00);
INSERT INTO member (memberno, title, firstname, surname, addressline1, addressline2, addressline3, addressline4, postcode, email, dob, rollcall, totalinvestment)
VALUES (3, 'Mrs', 'Rebecca', 'Hurst', '76 Clifton Road', 'Prestwich', 'Manchester', null, 'M25 3HR', 'rebeccajphillips8@gmail.com', '19/05/1970', '1', 500.00);
INSERT INTO member (memberno, title, firstname, surname, addressline1, addressline2, addressline3, addressline4, postcode, email, dob, rollcall, totalinvestment)
VALUES (4, 'Squadron Leader', 'Test', 'Test-Member', '76 Clifton Road', 'Prestwich', 'Manchester', null, 'M25 3HR', 'someone@somewhere.com', '18/09/1934', '1', 230.00);
