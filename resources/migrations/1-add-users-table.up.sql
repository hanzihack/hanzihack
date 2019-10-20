CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE ,
    name TEXT,
    picture TEXT,
    admin BOOLEAN NOT NULL DEFAULT FALSE,
    last_login TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);
--;;

INSERT INTO users(email, name, admin, is_active)
VALUES ('supakorn@hanzihack.com','Ziko',true,true);
