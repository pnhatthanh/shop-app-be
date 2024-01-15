CREATE DATABASE shopapp;
USE shopapp;
CREATE TABLE user(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fullname varchar(100) DEFAULT '',
    date_of_birth date,
    phone_number varchar(10) NOT NULL,
    address varchar(200) DEFAULT '',
    password varchar(100) not null DEFAULT '',
    create_at datetime,
    update_at datetime,
    is_active tinyint(1) DEFAULT 1,
    facebook_account_id int DEFAULT 0,
    google_account_id int DEFAULT 0
);

create table role(
    id int primary key,
    name_role varchar(20) not null
)

alter table user
add columns role_id int;
alter table user
add foreign key (role_id) references role(id);

create table token(
    id int PRIMARY key AUTO_INCREMENT,
    token varchar(255) unique not null,
    token_type varchar(50) not null,
    expiration_date datetime,
    revoked tinyint(1) not null,
    expired tinyint(1) not null,
    user_id int,
    foreign key (user_id) references user(id)
);

create table social_account(
    id int PRIMARY key AUTO_INCREMENT,
    provider varchar(100) not null,
    email varchar(150) not null,
    name varchar(100) not null
    user_id int,
    foreign key (user_id) references user(id)
);

create table category(
    id int primary key AUTO_INCREMENT,
    name_category varchar(100) not null DEFAULT ''
);

create table product(
    id int primary key AUTO_INCREMENT,
    name_product varchar(300) not null DEFAULT '',
    id_category int,
    price float not null check(price>=0),
    thumbnail varchar(300) not null DEFAULT '',
    description longtext default '',
    create_at datetime,
    update_at datetime,
    foreign key (id_category) references category(id)    
);

create table order(
    id int primary key AUTO_INCREMENT,
    id_user int,
    foreign key (user_id) references user(id),
    fullname varchar(100) default '',
    email varchar(100) default '',
    phone_number varchar(10) not null,
    address varchar(200) not null,
    note varchar(100) default '',
    order_date datetime default current_timestamp,
    status varchar(20),
    total_money float check(total_money>=0)
    shipping_method varchar(100),
    shipping_address varchar(200),
    shippng_date date,
    tracking_number varchar(100),
    payment_method varchar(100)
    column_active tinyint(1) -- Xóa đơn hàng là xóa mềm
);

alter table order
modify column status enum('pending','processing','shipped','delivered','cancelled');

create table detail_order(
    id int primary key AUTO_INCREMENT,
    id_order int,
    foreign key (order_id) references order(id),
    product_id int,
    foreign key (product_id) references product(id),
    price float check(price>=0),
    number_of_product int check(number_of_product>0),
    total_money float check(total_money>=0),
    color varchar(20) default ''
);

