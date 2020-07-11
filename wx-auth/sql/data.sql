create database auth;

use auth;

create table user(
    id int(11) not null primary key auto_increment,
    account varchar(20) not null,
    password varchar(20) not null,
    nickname varchar(20) not null,
    openid varchar(50)
);

insert into user(account, password, nickname) values('test', '123', 'test');