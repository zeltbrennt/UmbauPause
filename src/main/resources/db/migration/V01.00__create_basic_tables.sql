create schema if not exists shop;
create schema if not exists "user";

create table if not exists "user".account
(
    id            uuid primary key not null,
    created_at    timestamp        not null,
    updated_at    timestamp        not null,
    email         varchar(100)     not null,
    password_hash varchar(60)      not null
);

create table if not exists "user".role
(
    id   serial primary key,
    name varchar(50) not null
);

insert into "user".role (id, name)
values (0, 'ADMIN'),
       (1, 'USER')
on conflict do nothing;

create table if not exists "user".acc_role
(
    user_id uuid not null,
    role_id int  not null,
    primary key (user_id, role_id),
    foreign key (user_id) references "user".account (id),
    foreign key (role_id) references "user".role (id)
);

create table if not exists shop.dish
(
    id          serial primary key,
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    description varchar(250) not null
);

create table if not exists shop.menu
(
    id          serial primary key,
    created_at  timestamp not null,
    updated_at  timestamp not null,
    valid_from  date      not null,
    valid_to    date      not null,
    day_of_week int       not null,
    dish_id     integer   not null,
    foreign key (dish_id) references shop.dish (id),
    constraint menu_day_unique unique (valid_from, valid_to, day_of_week)
);

create index if not exists menu_valid_from on shop.menu (valid_from);
create index if not exists menu_valid_to on shop.menu (valid_to);

create table if not exists shop.order
(
    id         uuid primary key,
    created_at timestamp   not null,
    updated_at timestamp   not null,
    user_id    uuid        not null,
    menu_id    integer     not null,
    status     varchar(50) not null,
    foreign key (user_id) references "user".account (id),
    foreign key (menu_id) references shop.menu (id),
    constraint order_user_menu_unique unique (user_id, menu_id)
);