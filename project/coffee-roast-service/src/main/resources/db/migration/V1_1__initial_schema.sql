create table roast_task
(
    id                  bigserial   not null primary key,
    order_id            bigint      not null unique,
    customer_id         bigint      not null,
    roast_status        varchar(50) not null
);

create table roast_item
(
    id               bigserial      not null primary key,
    roast_task_id bigint         references roast_task(id),
    coffee           varchar(50)    not null,
    roast_degree     varchar(50)    not null,
    weight           int            not null
);