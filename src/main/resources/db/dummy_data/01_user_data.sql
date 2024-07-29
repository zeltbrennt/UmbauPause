insert into "user".account(id, created_at, updated_at, email, password_hash)
values (gen_random_uuid(), now(), now(), 'alice@example.com', ''),
       (gen_random_uuid(), now(), now(), 'bob@example.com', ''),
       (gen_random_uuid(), now(), now(), 'admin@example.com', '')
;

insert into "user".acc_role(user_id, role_id)
select id, 1
from "user".account;

insert into "user".acc_role(user_id, role_id)
select id, 0
from "user".account
where email = 'admin@example.com';
