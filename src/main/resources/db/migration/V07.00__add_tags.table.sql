create table if not exists shop.tag
(
    id         serial primary key,
    name       varchar(255) not null,
    times_used int          not null default 0
);

create table if not exists shop.dish_tag
(
    dish_id integer not null,
    tag_id  integer not null,
    primary key (dish_id, tag_id),
    foreign key (dish_id) references shop.dish (id),
    foreign key (tag_id) references shop.tag (id)
);

