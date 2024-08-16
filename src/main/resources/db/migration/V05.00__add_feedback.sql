create schema if not exists app;

create table if not exists app.feedback
(
    id         serial primary key,
    created_at timestamp default now() not null,
    author     varchar(100)            not null,
    feedback   text                    not null
);

