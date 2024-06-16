drop table if exists users, items, bookings, comments;

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
    available boolean,
    last_booking_id bigint,
    next_booking_id bigint
);

create table if not exists bookings (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    booker_id bigint not null references users(id),
    item_id bigint not null references items(id),
    status varchar(20) not null,
    start_time timestamp without time zone,
    end_time timestamp without time zone
);

create table if not exists comments (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text varchar(500) not null,
    author_name_id bigint not null references users(id),
    item_id bigint references items(id),
    created timestamp without time zone
);

-- alter table items
--    add constraint fk_items_comments
--    foreign key (item_id)
--    REFERENCES comments (id);