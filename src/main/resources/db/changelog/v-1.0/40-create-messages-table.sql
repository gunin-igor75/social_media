
create table if not exists messages
(
    id              bigserial primary key,
    content         varchar(255) not null,
    sender_id       bigint not null ,
    recipient_id    bigint not null,
    foreign key (sender_id) references users(id),
    foreign key (recipient_id) references users(id)

);

GO