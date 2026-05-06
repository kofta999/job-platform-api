alter table if exists software_engineer
    add column user_id integer not null;

alter table if exists users
    add column email varchar(255);

alter table if exists users
    add column is_enabled boolean;

alter table if exists users
    alter column password set data type varchar(255);

alter table if exists users
    add column role varchar(255) not null check (role in ('ROLE_ENGINEER','ROLE_COMPANY','ROLE_ADMIN'));

alter table if exists users
    add column company_id integer;

alter table if exists software_engineer
    drop constraint if exists uk_software_engineer_user_id;

alter table if exists software_engineer
    add constraint uk_software_engineer_user_id unique (user_id);

alter table if exists users
    drop constraint if exists uk_users_email;

alter table if exists users
    add constraint uk_users_email unique (email);

alter table if exists software_engineer
    add constraint fk_software_engineer_user_id
    foreign key (user_id)
    references users;

alter table if exists users
    add constraint fk_users_company_id
    foreign key (company_id)
    references company;