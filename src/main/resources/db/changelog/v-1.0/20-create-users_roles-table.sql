
create table if not exists users_roles (

    role_id         integer not nul,
    users_id        integer not nul ,
     foreign key (user_id) references users(id),
    foreign key (role_id) references roles(id)

);

GO