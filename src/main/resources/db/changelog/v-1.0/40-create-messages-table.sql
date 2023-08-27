
create table if not exists messages
(
    id              bigserial primary key,
    content         varchar(255) not null,
    created_at      timestamp(6) with time zone,
    sender_id       bigint not null ,
    recipient       bigint not null,
    foreign key (sender_id) references users(id)
);

GO