ALTER TABLE member
ADD committee boolean default false;

UPDATE member SET committee = true WHERE memberno IN (1, 4, 5, 31, 189, 288, 332, 342);
