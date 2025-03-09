alter table shop.tag
    add constraint unique_tag_name unique (name);