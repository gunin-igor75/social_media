
create table if not exists users_friends
(
    friends_id bigint not null,
    user_id bigint not null,
    primary key (friends_id, user_id),
    foreign key (user_id)  references users(id),
    foreign key (friends_id)  references users(id)

);

GO