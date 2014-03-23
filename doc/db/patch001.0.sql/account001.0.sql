DROP TABLE account;
CREATE TABLE account
(
	id       bigint                 PRIMARY KEY,
	email    varchar(200)           not null,
	password varchar(200)           not null,
	role     varchar(20)            not null,
	CONSTRAINT email_uk             UNIQUE(email)
);

INSERT INTO account (id, email, password, role)
VALUES (34, 'user', '343fd6f6ac64c271c1b4224b72d8693a13d7605ff51c9140fe3ea93661a07fd4b32f58403d8de5c0', 'ROLE_USER');
INSERT INTO account (id, email, password, role)
VALUES (35, 'admin', 'c865b5cd2929980a6e4a64bb40e9c135fbd605c9e6132580ff97b75caa6b3688c381bf03fb49a3af', 'ROLE_ADMIN');
INSERT INTO account (id, email, password, role)
VALUES (36, 'jhurst1970@gmail.com', '1b267c3e4c3fbdecf976b86bf84a1afe580aca97338459c1b14a9a8e200da08e2bbe5d0f44ae58f6', 'ROLE_ADMIN');

