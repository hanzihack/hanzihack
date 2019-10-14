CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email TEXT,
    password TEXT,
    name TEXT,
    admin BOOLEAN,
    last_login TIMESTAMP,
    is_active BOOLEAN
);
