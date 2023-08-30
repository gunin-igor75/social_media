
create table if not exists invites
(
    id  bigserial primary key,
    candidate bigint,
    created_at timestamp(6) with time zone,
    status varchar(20),
    user_id bigint,
    foreign key (user_id) references users(id)
);

GO