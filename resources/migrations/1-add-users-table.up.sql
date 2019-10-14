CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE ,
    password TEXT,
    name TEXT,
    admin BOOLEAN NOT NULL,
    last_login TIMESTAMP,
    is_active BOOLEAN NOT NULL
);
--;;

INSERT INTO users(email, name, admin, is_active)
VALUES ('supakorn@hanzihack.com','Ziko',true,true);
