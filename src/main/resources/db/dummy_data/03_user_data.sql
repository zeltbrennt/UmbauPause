insert into shop.user(id, created_at, updated_at, email, password_hash, role)
values (gen_random_uuid(), now(), now(), 'alice@example.com', '', 'USER'),
       (gen_random_uuid(), now(), now(), 'bob@example.com', '', 'USER'),
       (gen_random_uuid(), now(), now(), 'admin@example.com', '', 'ADMIN'),
       (gen_random_uuid(), now(), now(), 'mod@example.com', '', 'MODERATOR')
