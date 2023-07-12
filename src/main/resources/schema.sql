CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar( 255) NOT NULL,
    email varchar(255) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE(email)
);


CREATE TABLE IF NOT EXISTS items
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL,
    description varchar(1000) NOT NULL,
    is_available boolean NOT NULL,
    request_id BIGINT,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_items_to_user
        FOREIGN KEY(owner_id) REFERENCES users(id)

);

CREATE TYPE Status AS ENUM('WAITING','APPROVED','REJECTED','CANCELED');

CREATE TABLE IF NOT EXISTS bookings
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date timestamp,
    end_date timestamp,
    item_id BIGINT,
    booker_id BIGINT,
    status Status,
    CONSTRAINT fk_booking_to_item
        FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT fk_booking_to_user
        FOREIGN KEY (booker_id) REFERENCES users(id)

);


CREATE TABLE IF NOT EXISTS comments
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text varchar(255) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created timestamp NOT NULL,
    CONSTRAINT fk_comment_to_user
        FOREIGN KEY(author_id) REFERENCES users(id),
    CONSTRAINT fk_comment_to_item
        FOREIGN KEY(item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description varchar(1000),
    requestor_id BIGINT,
    created timestamp NOT NULL

);