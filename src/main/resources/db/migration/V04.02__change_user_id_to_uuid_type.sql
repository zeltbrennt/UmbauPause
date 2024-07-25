alter table shop."user"
    alter column user_uuid type uuid using user_uuid::uuid;


