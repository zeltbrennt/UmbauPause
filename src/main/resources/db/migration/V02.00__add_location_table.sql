create table if not exists shop.location
(
    id   serial primary key,
    name varchar(100) not null
);

insert into shop.location (id, name)
values (0, 'Haupthaus')
on conflict do nothing;

alter table shop.order
    add column location_id integer default 1 not null,
    add foreign key (location_id) references shop.location (id);

