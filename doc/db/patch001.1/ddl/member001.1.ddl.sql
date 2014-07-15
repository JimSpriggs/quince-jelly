/*
 * Remember to SET ROLE vgowner;
 */
ALTER TABLE member
ADD memberno integer;

UPDATE member SET memberno = id;

CREATE SEQUENCE member_no_seq;

ALTER TABLE member
ADD member_status_cd VARCHAR(20),
ADD FOREIGN KEY (member_status_cd) REFERENCES member_status(member_status_cd)
;

UPDATE member SET member_status_cd = 'FULL';

ALTER TABLE member
ALTER member_status_cd SET NOT NULL;