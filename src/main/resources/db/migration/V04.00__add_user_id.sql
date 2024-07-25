alter table shop."user"
    rename column username to user_uuid;

update shop."user"
set user_uuid = gen_random_uuid();

alter table shop."user"
    add constraint user_uuid unique (user_uuid);
