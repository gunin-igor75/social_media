
create table if not exists roles (

    id          serial primary key,
    name        varchar(255) not null constraint nameUnique unique,
);

GO