-- coffee

create table roast_degree
(
    id   bigserial   not null primary key,
    name varchar(50) not null
);

create table coffee
(
    id              bigserial   not null primary key,
    name            varchar(50) not null,
    roast_degree_id bigint      not null references roast_degree(id)
);

-- customer

create table customer
(
    id    bigserial   not null primary key,
    login varchar(50) not null unique
);

create table address
(
    id               bigserial   not null primary key,
    city             varchar(50) not null,
    street           varchar(50) not null,
    house_number     varchar(50) not null,
    apartment_number varchar(50)
);

create table customer_address
(
    customer_id bigint not null references customer(id),
    address_id  bigint not null references address(id)
);

-- order

create table package_size
(
    id     serial not null primary key,
    weight int    not null
);
create table coffee_order
(
    id           bigserial   not null primary key,
    customer_id  bigint      not null references customer(id),
    address_id   bigint      not null references address(id),
    order_status varchar(50) not null
);
create table order_item
(
    id                bigserial not null primary key,
    order_id          bigint    references coffee_order(id),
    coffee_id         bigint    not null references coffee(id),
    package_size_id   int       not null references package_size(id),
    count_of_packages smallint  not null
);