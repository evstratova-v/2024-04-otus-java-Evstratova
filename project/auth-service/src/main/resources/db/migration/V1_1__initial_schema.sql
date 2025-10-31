create table users
(
    id       bigserial   not null primary key,
    username varchar(30) not null,
    password varchar(60) not null
);

create table roles
(
    id   bigserial    not null primary key,
    name varchar(100) not null
);

create table users_roles
(
    user_id bigint not null references users(id),
    role_id bigint not null references roles(id)
);
