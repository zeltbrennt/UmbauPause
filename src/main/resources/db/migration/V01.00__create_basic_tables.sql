drop schema if exists shop cascade;
create schema shop;

create table shop.user
(
    id            uuid primary key not null,
    created_at    timestamp        not null,
    updated_at    timestamp        not null,
    email         varchar(100)     not null,
    password_hash varchar(60)      not null,
    role          varchar(100)     not null
);

create table shop.dish
(
    id          serial primary key,
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    description varchar(250) not null
);


create table shop.week
(
    id         serial primary key,
    week_start date not null,
    week_end   date not null,
    kw         int  not null,
    year       int  not null,
    quarter    int  not null
);

create table shop.day
(
    id   int primary key,
    name varchar(10) not null
);

create table shop.menu
(
    id         serial primary key,
    created_at timestamp not null,
    updated_at timestamp not null,
    week_id    integer   not null,
    dish_id    integer   not null,
    day_id     int       not null,
    foreign key (dish_id) references shop.dish (id),
    foreign key (week_id) references shop.week (id),
    foreign key (day_id) references shop.day (id),
    constraint menu_day_unique unique (week_id, day_id)
);

create table shop.order
(
    id         uuid primary key,
    created_at timestamp   not null,
    updated_at timestamp   not null,
    user_id    uuid        not null,
    menu_id    integer     not null,
    status     varchar(50) not null,
    foreign key (user_id) references shop.user (id),
    foreign key (menu_id) references shop.menu (id),
    constraint order_user_menu_unique unique (user_id, menu_id)
);