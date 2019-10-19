
CREATE SCHEMA marilyn;
--;;
CREATE TABLE marilyn.groups(
    id BIGSERIAL PRIMARY KEY,
    name TEXT UNIQUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--;;
INSERT INTO marilyn.groups(name)
VALUES ('a'),('i'),('u'),('ü');
--;;
CREATE TABLE marilyn.initials(
    id BIGSERIAL PRIMARY KEY,
    sound TEXT,
    "group" TEXT REFERENCES marilyn.groups(name),
    pinyin TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--;;
CREATE TABLE marilyn.finals(
    id BIGSERIAL PRIMARY KEY,
    sound TEXT,
    pinyin TEXT,
    pinyin_compact TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--;;
CREATE TABLE marilyn.tones(
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--;;
INSERT INTO marilyn.tones(id,name)
VALUES (1,'High'),
       (2,'Rising'),
       (3,'Falling and Rising'),
       (4,'Falling');
--;;
CREATE TABLE marilyn.character(
    id BIGSERIAL PRIMARY KEY,
    writing TEXT NOT NULL,
    pinyin TEXT NOT NULL ,
    meaning TEXT,
    initial_id INT REFERENCES marilyn.initials(id),
    final_id INT REFERENCES marilyn.finals(id),
    tone INT REFERENCES marilyn.tones(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
--;;
-- INSERT INTO marilyn.character(writing,meaning,initial_id,final_id,tone)
-- VALUES ('元','Yuan - Chinese Currency','idOf(-,uv)','an',2)




