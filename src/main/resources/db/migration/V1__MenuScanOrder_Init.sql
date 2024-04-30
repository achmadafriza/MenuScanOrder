create table menu
(
    id   int auto_increment
        primary key,
    name varchar(128) null
);

create unique index menu_name_uindex
    on menu (name);

create table restaurant
(
    id             int auto_increment
        primary key,
    name           varchar(128)         not null,
    description    varchar(512)         null,
    profile_pic    varchar(2048)        null,
    background_pic varchar(2048)        null,
    is_active      tinyint(1) default 1 not null
);

create table category
(
    id            int auto_increment
        primary key,
    restaurant_id int          not null,
    name          varchar(128) not null,
    constraint category_restaurant_id_fk
        foreign key (restaurant_id) references restaurant (id)
            on update cascade
);

create table menu_item
(
    id            int auto_increment
        primary key,
    restaurant_id int                                 not null,
    category_id   int                                 not null,
    name          varchar(128)                        not null,
    description   varchar(512)                        null,
    price         int                                 not null,
    menu_pic      varchar(2048)                       null,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    updated_at    timestamp default CURRENT_TIMESTAMP not null,
    constraint menu_item_category_id_fk
        foreign key (category_id) references category (id)
            on update cascade
            on delete cascade,
    constraint menu_item_restaurant_id_fk
        foreign key (restaurant_id) references restaurant (id)
            on update cascade
);

create table role
(
    id   int auto_increment
        primary key,
    name varchar(128) null
);

create unique index role_name_uindex
    on role (name);

create table role_menu_mapping
(
    role_id int not null,
    menu_id int not null,
    primary key (role_id, menu_id),
    constraint role_menu_mapping_menu_id_fk
        foreign key (menu_id) references menu (id)
            on update cascade on delete cascade,
    constraint role_menu_mapping_role_id_fk
        foreign key (role_id) references role (id)
            on update cascade on delete cascade
);

create table restaurant_table
(
    restaurant_id int                  not null,
    table_number  int                  not null,
    uuid          varchar(36)          not null,
    is_active     tinyint(1) default 1 not null,
    primary key (restaurant_id, table_number),
    constraint uuid
        unique (uuid),
    constraint table_restaurant_id_fk
        foreign key (restaurant_id) references restaurant (id)
            on update cascade
);

create table restaurant_order
(
    id            int auto_increment
        primary key,
    restaurant_id int                                   not null,
    table_number  int                                   not null,
    status        enum('POSTED','RECEIVED','FINISHED','CANCELLED') default 'POSTED'          not null,
    ordered_at    timestamp   default CURRENT_TIMESTAMP not null,
    updated_at    timestamp   default CURRENT_TIMESTAMP not null,
    constraint order_table_restaurant_id_table_number_fk
        foreign key (restaurant_id, table_number) references restaurant_table (restaurant_id, table_number)
            on update cascade
);

create table order_menu_item
(
    id       int auto_increment
        primary key,
    order_id int           not null,
    name     varchar(128)  not null,
    price    int           not null,
    quantity int           not null,
    notes    varchar(2048) null,
    constraint order_menu_item_order_id_fk
        foreign key (order_id) references restaurant_order (id)
            on update cascade
);

create table user
(
    id            int auto_increment
        primary key,
    role_id       int                                  not null,
    restaurant_id int                                  null,
    email         varchar(320)                         not null,
    password      varchar(64)                          not null,
    is_active     tinyint(1) default 1                 not null,
    created_at    timestamp  default CURRENT_TIMESTAMP not null,
    updated_at    timestamp  default CURRENT_TIMESTAMP not null,
    constraint email
        unique (email),
    constraint user_restaurant_id_fk
        foreign key (restaurant_id) references restaurant (id)
            on update cascade,
    constraint user_role_id_fk
        foreign key (role_id) references role (id)
            on update cascade
);

