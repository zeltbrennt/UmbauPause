create table if not exists shop.role
(
    id   serial primary key,
    role varchar(50) not null
);

insert into shop.role (id, role)
values (0, 'ADMIN'),
       (1, 'USER');

create table if not exists shop.user_role
(
    user_id uuid not null,
    role_id int  not null,
    primary key (user_id, role_id),
    foreign key (user_id) references shop.user (id),
    foreign key (role_id) references shop.role (id)
);

-- default role is USER
insert into shop.user_role (user_id, role_id)
select id, 1
from shop.user;


alter table shop.user
    drop column role;
