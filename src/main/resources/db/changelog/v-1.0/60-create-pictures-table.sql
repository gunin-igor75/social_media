
create table if not exists posts
(
    id bigint primary key,
    file_size bigint not null,
    file_path varchar(255) not null,
    media_type varchar(255) not null,
    foreign key (id) references posts(id),

);

GO