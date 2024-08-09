alter table "user".account
    add constraint unique_email unique (email);

alter table "shop".location
    add constraint unique_name unique (name);

