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
    deadline            date not null default now(),
    version            	int not null default 0,
    created_by         	varchar(100),
    last_modified_by   	varchar(100),
    created_date        timestamp,
    last_modified_date  timestamp,
    foreign key (executor_id) references users (id),
    foreign key (controller_id) references users (id)
    );

create table if not exists tasks_history(
    id              	bigserial primary key,
    operation           varchar(10),
    task_id             bigint not null,
    executor_id_new     bigint,
    controller_id_new   bigint,
    title_new           varchar(255),
    description_new     text not null,
    deadline_new        date not null,
    version            	int not null,
    modified_by   	    varchar(100),
    modified_date       timestamp
    );

CREATE OR REPLACE FUNCTION tasks_history() RETURNS trigger AS
$BODY$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        INSERT INTO tasks_history(operation, task_id, executor_id_new, controller_id_new, title_new, description_new, deadline_new, version, modified_by, modified_date)
        VALUES ('UPDATE', old.id, new.executor_id, new.controller_id, new.title, new.description, new.deadline, new.version, new.last_modified_by, current_timestamp);
        RETURN NEW;

    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO tasks_history(operation, task_id, executor_id_new, controller_id_new, title_new, description_new, deadline_new, version, modified_by, modified_date)
        VALUES ('INSERT', new.id, new.executor_id, new.controller_id, new.title, new.description, new.deadline, new.version, new.last_modified_by, current_timestamp);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER history_tasks
    AFTER INSERT OR UPDATE ON tasks
    FOR EACH ROW EXECUTE PROCEDURE tasks_history();