
create table if not exists friends
(
    id              bigserial primary key,
    users_id        bigint not null ,
    friend_id       bigint not null,
    foreign key (user_id)  references users(id),
    foreign key (friend_id)  references users(id)

);

GO