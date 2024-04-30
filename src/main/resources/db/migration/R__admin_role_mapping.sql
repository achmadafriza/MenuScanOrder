insert into  role (id, name) values (1, 'ADMIN') on duplicate key update name = 'ADMIN';

replace into menu (id, name)
values (1, 'ADD_USER'),
       (2, 'EDIT_USER'),
       (3, 'DELETE_USER'),
       (4, 'EDIT_RESTAURANT'),
       (5, 'MANAGE_SUBSCRIPTION');

replace into role_menu_mapping (role_id, menu_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5);

# password: admin
replace into user (id, role_id, restaurant_id, email, password)
values (1, 1, null, 'admin@example.com', '$2y$10$Y9AcCSJmCctbbKIQx3ijV.g0bp0opJuocjmcbLxeitEXI.UVpm.Cm');