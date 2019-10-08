CREATE TABLE marilyn.actors (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    initial_id BIGSERIAL REFERENCES marilyn.initials(id),
    user_id BIGSERIAL REFERENCES public.users(id),
    created_at TIMESTAMPTZ
);
