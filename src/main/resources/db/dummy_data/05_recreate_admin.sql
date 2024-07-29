insert into shop.user_role (user_id, role_id)
select id, 0
from shop.user
where email = 'admin@example.com';