
create table if not exists users_roles (

    role_id     integer not null,
    user_id     bigint not null ,
    foreign key (user_id) references users(id),
    foreign key (role_id) references roles(id)

);

GO