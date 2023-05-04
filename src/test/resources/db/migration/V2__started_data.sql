insert into users(username, password, email)
values
    ('admin', '$2a$10$tZDY1/sJwv7CmNLt.yDAzeRqwHnuyNHEIT/Id5AWYHgOmFyv8TG.6', 'taskeditor.service@gmail.com'), --password13
    ('ivanovaa', '$2a$12$n6KY4mpI2RdcBQWwyM4msudGmPD6gXxQDjKDQiXojmzykVYhtvaem', 'ivanovaa@tasks-editor.ru'),   --id 2 user     100
    ('petrovss', '$2a$12$n6KY4mpI2RdcBQWwyM4msudGmPD6gXxQDjKDQiXojmzykVYhtvaem',  'petrovss@tasks-editor.ru');   --id 3 user     100


insert into roles(name)
values
    ('ROLE_ADMIN'), --1
    ('ROLE_USER'); --2

insert into users_roles (user_id, role_id)
values
    (1, 1),   -- admin
    (2, 2), (3, 2); --users