INSERT INTO member_consent (member_id, marketing_in, creation_ts, update_ts) SELECT id, false, NOW(), NOW() FROM member;