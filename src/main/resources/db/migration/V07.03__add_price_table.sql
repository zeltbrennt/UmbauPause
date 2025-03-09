create table shop.price
(
    id    serial primary key,
    type  varchar(255) not null,
    price integer      not null
);

insert into shop.price (id, type, price)
values (0, 'default', 690)