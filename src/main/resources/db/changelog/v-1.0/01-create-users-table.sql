
create table if not exists users (

    id          bigserial primary key,
    username    varchar(255) not null unique,
    name        varchar(255) not null,
    password    varchar(255) not null
);

GO

