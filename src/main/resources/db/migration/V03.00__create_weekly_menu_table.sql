create table shop.weekly_menu
(
    id         serial primary key,
    monday     varchar(250),
    tuesday    varchar(250),
    wednesday  varchar(250),
    thursday   varchar(250),
    friday     varchar(250),
    valid_from date,
    valid_to   date
);
