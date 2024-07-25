alter table shop."dish"
    drop column available;

alter table shop."dish"
    drop column scheduled;

alter table shop.dish
    add column dish_uuid uuid default gen_random_uuid();

alter table shop."dish"
    add constraint dish_uuid unique (dish_uuid);


