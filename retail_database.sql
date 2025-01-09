create database retail;
use retail;

create table items (
upc BIGINT,
item_name varchar(12),
price DECIMAL(10, 2),
PRIMARY KEY (upc)
);

create table operators (
id int,
pin int,
name varchar(255),
PRIMARY KEY	(id)
);

create table transactions (
transaction_num BIGINT,
op_num int,
date DATETIME,
payment_method varchar(50),
PRIMARY KEY (transaction_num),
FOREIGN KEY (op_num) REFERENCES operators(id)
);

create table transaction_items (
transaction_item_num int AUTO_INCREMENT,
transaction_num BIGINT,
upc BIGINT,
quantity int,
PRIMARY KEY (transaction_item_num),
FOREIGN KEY (transaction_num) REFERENCES transactions(transaction_num),
FOREIGN KEY (upc) REFERENCES items(upc)
);

insert into items (upc, item_name, price)
values (045496882266, "NSWLite Turq", 199.99);

insert into operators (id, pin, name)
values (6246, 3571, "Sherry Nguyen");

set foreign_key_checks = 0;
truncate table transactions;
truncate table transactions_items;
set foreign_key_checks = 1;