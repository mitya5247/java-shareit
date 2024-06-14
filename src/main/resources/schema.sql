drop table if exists users, items, bookings;

create table if not exists users (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(120) not null unique,
    name varchar(100)
);

create table if not exists items (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(100) not null,
    description varchar(500) not null,
    owner bigint,
    available boolean
);

create table if not exists bookings (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    booker_id bigint not null references users(id),
    item_id bigint not null references items(id),
    status varchar(20) not null,
    start_time timestamp without time zone,
    end_time timestamp without time zone
);