insert into shop.dish (id, created_at, updated_at, description)
values (999900, now(), now(), 'Test Dish 1'),
       (999901, now(), now(), 'Test Dish 2'),
       (999902, now(), now(), 'Test Dish 3'),
       (999903, now(), now(), 'Test Dish 4'),
       (999904, now(), now(), 'Test Dish 5'),
       (0, now(), now(), '')
on conflict do nothing;


insert into shop.menu (id, created_at, updated_at, valid_from, valid_to, day_of_week, dish_id)
-- full week
values (999900, now(), now(), date('9999-01-01'), date('9999-01-05'), 1, 999900),
       (999901, now(), now(), date('9999-01-01'), date('9999-01-05'), 2, 999901),
       (999902, now(), now(), date('9999-01-01'), date('9999-01-05'), 3, 999902),
       (999903, now(), now(), date('9999-01-01'), date('9999-01-05'), 4, 999903),
       (999904, now(), now(), date('9999-01-01'), date('9999-01-05'), 5, 999904),
       -- week with missing day
       (999905, now(), now(), date('9999-01-08'), date('9999-01-12'), 1, 999900),
       (999906, now(), now(), date('9999-01-08'), date('9999-01-12'), 2, 999901),
       (999907, now(), now(), date('9999-01-08'), date('9999-01-12'), 3, 999902),
       (999908, now(), now(), date('9999-01-08'), date('9999-01-12'), 4, 999903),
       (999909, now(), now(), date('9999-01-08'), date('9999-01-12'), 5, 0),
       -- full week without dishes
       (999910, now(), now(), date('9999-01-15'), date('9999-01-19'), 1, 0),
       (999911, now(), now(), date('9999-01-15'), date('9999-01-19'), 2, 0),
       (999912, now(), now(), date('9999-01-15'), date('9999-01-19'), 3, 0),
       (999913, now(), now(), date('9999-01-15'), date('9999-01-19'), 4, 0),
       (999914, now(), now(), date('9999-01-15'), date('9999-01-19'), 5, 0);



