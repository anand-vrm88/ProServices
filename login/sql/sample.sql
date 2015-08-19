drop database if exists login;

create database if not exists login;

use login;

create table if not exists APPLICATION_ACCOUNTS (
	APP_ID int not null auto_increment,
	APP_NAME varchar(40) not null,
	DOMAIN_NAME varchar(50) not null,
	PORT_NUMBER int,
	APP_CONTEXT varchar(40) not null,
	PUBLIC_RESOURCE varchar(50),
	primary key(APP_ID)
) auto_increment=100;

create table if not exists USER_ACCOUNTS (
	USERNAME varchar(40) not null,
	APP_ID int not null,
	PASSWORD varchar(40) not null,
	primary key(USERNAME, APP_ID)
);

create table if not exists USER_SESSION (
	SESSION_ID int not null,
	TOKEN varchar(50) not null,
	USERNAME varchar(40) not null,
	CREATION_TIME timestamp not null default current_timestamp,
	LAST_ACTIVE_TIME timestamp not null,
	primary key(SESSION_ID)
);

create table if not exists USER_APPLICATIONS (
	USERNAME varchar(40) not null,
	APP_ID int not null,
	primary key(USERNAME, APP_ID)
);

#----------------following tables might be used in future -------------------------------------------
create table if not exists USER_GROUPS (
	GROUP_ID int not null auto_increment,
	GROUPNAME varchar(20) not null,
	primary key(GROUP_ID)
) auto_increment=100;

create table if not exists USER_PROFILE (
	PROFILE_ID int unsigned not null auto_increment,
	USERNAME varchar(40) not null,
	EMAIL_ID varchar(40),
	FIRST_NAME varchar(20) not null,
	LAST_NAME varchar(20),
	GENDER varchar(15) not null,
	CITY varchar(20),
	STATE varchar(20),
	COUNTRY varchar(20),
	primary key(PROFILE_ID),
	index(USERNAME)
) auto_increment=10000;
#----------------------------------------------------------------------------------------------------

#insert into APPLICATION_ACCOUNTS(APP_NAME, APP_URL) values('SciThoughts', 'http://localhost:8181/scithoughts');

insert into APPLICATION_ACCOUNTS(APP_NAME, DOMAIN_NAME, PORT_NUMBER, APP_CONTEXT, PUBLIC_RESOURCE) values('SmartIdea', 'smartidea.anverm.com', 8181, '/', null);
insert into APPLICATION_ACCOUNTS(APP_NAME, DOMAIN_NAME, PORT_NUMBER, APP_CONTEXT, PUBLIC_RESOURCE) values('login', 'login.anverm.com', 8181, '/', null);

insert into USER_ACCOUNTS values('anverm', 100,'cool');
insert into USER_ACCOUNTS values('anverm', 101,'cool');


insert into USER_APPLICATIONS values('anverm', 100);
insert into USER_APPLICATIONS values('anverm', 101);

#insert into USER_PROFILE(username, email_id, first_name, last_name) values ('anverm', 'anverm@gmail.com', 'Anand', 'Verma');