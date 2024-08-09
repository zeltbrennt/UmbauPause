delete
from shop.dish
where description like 'Test%';

delete
from shop.menu
where valid_from >= date('9999-01-01');
