--database PostgreSQL creation
create table if not exists users(
    id          bigserial primary key,
    username    varchar(100) unique not null,
    password    varchar(100) not null,
    email       varchar(50) not null
    );

create table if not exists roles(
    id          bigserial primary key,
    name        varchar(30) not null
    );

create table if not exists users_roles(
    user_id               bigint not null,
    role_id               bigint not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)
    );


create table if not exists tasks(
    id              	bigserial primary key,
    executor_id      	bigint not null,
    controller_id     	bigint,
    title              	varchar(255) not null,
    description     	text not null,
    version            	int not null default 0,
    created_by         	varchar(100),
    last_modified_by   	varchar(100),
    created_date        timestamp,
    last_modified_date  timestamp,
    foreign key (executor_id) references users (id),
    foreign key (controller_id) references users (id)
    );
