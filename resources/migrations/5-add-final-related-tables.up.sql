CREATE TABLE marilyn.locations (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL ,
    note TEXT,
    image TEXT,
    final_id BIGINT REFERENCES marilyn.finals(id),
    user_id BIGINT REFERENCES public.users(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT locations_unique_for_user UNIQUE (final_id, user_id),
    CONSTRAINT locations_unique_image_for_user UNIQUE (image, user_id)
);

--;;

CREATE TABLE marilyn.areas (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL ,
    image TEXT,
    tone_id BIGINT REFERENCES marilyn.tones(id),
    location_id BIGINT REFERENCES marilyn.locations(id),
    user_id BIGINT REFERENCES public.users(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT areas_unique_for_user UNIQUE (location_id, tone_id, user_id),
    CONSTRAINT areas_unique_image_for_user UNIQUE (image, user_id)
);

