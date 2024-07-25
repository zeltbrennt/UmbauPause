create table shop.order
(
    id         serial primary key,
    user_id    uuid         not null,
    dish_name  varchar(250) not null,
    status     varchar(100) not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    foreign key (user_id) references shop.user (user_uuid),
    foreign key (dish_name) references shop.dish (description)
);
