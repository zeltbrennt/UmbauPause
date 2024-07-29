insert into shop.menu (created_at, updated_at, valid_from, valid_to, day_of_week, dish_id)
values (now(), now(), date('2024-07-29'), date('2024-08-02'), 1, 1),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 2, 2),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 3, 3),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 4, 4),
       (now(), now(), date('2024-07-29'), date('2024-08-02'), 5, 5),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 1, 6),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 2, 7),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 3, 8),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 4, 9),
       (now(), now(), date('2024-08-05'), date('2024-08-09'), 5, 10);