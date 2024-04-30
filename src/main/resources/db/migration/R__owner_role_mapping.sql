insert into  role (id, name) values (2, 'OWNER') on duplicate key update name = 'OWNER';
insert into  role (id, name) values (3, 'STAFF') on duplicate key update name = 'STAFF';

replace into menu (id, name)
values (6, 'MANAGE_MENU'),
       (7, 'ADD_MENU'),
       (8, 'EDIT_MENU'),
       (9, 'ADD_CATEGORY'),
       (10, 'MANAGE_TABLE'),
       (11, 'MANAGE_ORDER'),
       (12, 'DELETE_MENU');

replace into role_menu_mapping (role_id, menu_id)
VALUES (2, 6),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (3, 11);