alter table shop.dish
    drop column dish_uuid;

alter table shop.dish
    add constraint dish_description_key unique (description);
