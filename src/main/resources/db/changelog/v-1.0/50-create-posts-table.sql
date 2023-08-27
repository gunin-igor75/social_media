
create table if not exists posts
(
    id              bigserial primary key,
    title           varchar(255) not null unique ,
    content         varchar(255) not null,
    image           varchar(255),
    created_at      timestamp(6) with time zone,
    updated_at      timestamp(6) with time zone,
    user_id       bigint not null ,
    foreign key (user_id)  references users(id)

);

GO