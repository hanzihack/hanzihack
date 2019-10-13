CREATE TABLE marilyn.actors (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    initial_id BIGINT REFERENCES marilyn.initials(id),
    user_id BIGINT REFERENCES public.users(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
