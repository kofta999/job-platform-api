alter table if exists company
    add column created_at timestamp(6) with time zone not null;

alter table if exists company
    add column updated_at timestamp(6) with time zone;

alter table if exists engineer_profile
    add column created_at timestamp(6) with time zone not null;

alter table if exists engineer_profile
    add column updated_at timestamp(6) with time zone;

alter table if exists job_application
    add column created_at timestamp(6) with time zone not null;

alter table if exists job_application
    add column updated_at timestamp(6) with time zone;

alter table if exists job_posting
    add column created_at timestamp(6) with time zone not null;

alter table if exists job_posting
    add column updated_at timestamp(6) with time zone;

alter table if exists skill
    add column created_at timestamp(6) with time zone not null;

alter table if exists skill
    add column updated_at timestamp(6) with time zone;

alter table if exists software_engineer
    add column created_at timestamp(6) with time zone not null;

alter table if exists software_engineer
    add column updated_at timestamp(6) with time zone;

alter table if exists users
    add column created_at timestamp(6) with time zone not null;

alter table if exists users
    add column updated_at timestamp(6) with time zone;
