TRUNCATE TABLE marilyn.initials;
--;;
TRUNCATE TABLE marilyn.finals;
--;;
INSERT INTO marilyn.finals(sound,pinyin)
VALUES ('-','-'),
       ('a','a'),
       ('e','e');
--;;
INSERT INTO marilyn.initials(sound,"group",pinyin)
VALUES ('p','a','p'),
       ('p','i','pi');