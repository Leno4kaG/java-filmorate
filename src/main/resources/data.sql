-- TRUNCATE TABLE PUBLIC.RATING;
MERGE INTO PUBLIC.RATING KEY (id) VALUES (1, 'G');
MERGE INTO PUBLIC.RATING KEY (id) VALUES (2, 'PG');
MERGE INTO PUBLIC.RATING KEY (id) VALUES (3, 'PG-13');
MERGE INTO PUBLIC.RATING KEY (id) VALUES (4, 'R');
MERGE INTO PUBLIC.RATING KEY (id) VALUES (5, 'NC-17');

-- TRUNCATE TABLE PUBLIC.GENRE;
MERGE INTO PUBLIC.GENRE KEY (id) VALUES (1, 'Комедия');
MERGE INTO PUBLIC.GENRE KEY (id) VALUES (2, 'Драма');
MERGE INTO PUBLIC.GENRE KEY (id) VALUES (3, 'Мультфильм');
MERGE INTO PUBLIC.GENRE KEY (id) VALUES (4, 'Триллер');
MERGE INTO PUBLIC.GENRE KEY (id) VALUES (5, 'Документальный');
MERGE INTO PUBLIC.GENRE KEY (id) VALUES (6, 'Боевик');