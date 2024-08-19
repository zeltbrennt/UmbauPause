alter table shop."order"
    add column if not exists valid_from date;
alter table shop."order"
    add column if not exists valid_to date;

update shop."order"
set valid_from = m.valid_from,
    valid_to   = m.valid_to
from shop.menu m
where m.id = menu_id;

alter table shop.menu
    drop constraint if exists valid_menu_unique;
alter table shop.menu
    add constraint valid_menu_unique unique (valid_from, valid_to, id);

alter table shop."order"
    drop constraint if exists order_valid_from_valid_to_menu_id_fkey;
alter table shop."order"
    add foreign key (valid_from, valid_to, menu_id) references shop.menu (valid_from, valid_to, id);
