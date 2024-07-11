create table shop.user
(
    id       serial primary key,
    username varchar(200) not null,
    email    varchar(200) not null,
    password varchar(250) not null,
    role     varchar(100) not null
);