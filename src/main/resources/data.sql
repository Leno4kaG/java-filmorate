-- TRUNCATE TABLE d_rating;
MERGE INTO d_rating KEY (id) VALUES (1, 'G');
MERGE INTO d_rating KEY (id) VALUES (2, 'PG');
MERGE INTO d_rating KEY (id) VALUES (3, 'PG-13');
MERGE INTO d_rating KEY (id) VALUES (4, 'R');
MERGE INTO d_rating KEY (id) VALUES (5, 'NC-17');

-- TRUNCATE TABLE d_genre;
MERGE INTO d_genre KEY (id) VALUES (1, 'Комедия');
MERGE INTO d_genre KEY (id) VALUES (2, 'Драма');
MERGE INTO d_genre KEY (id) VALUES (3, 'Мультфильм');
MERGE INTO d_genre KEY (id) VALUES (4, 'Триллер');
MERGE INTO d_genre KEY (id) VALUES (5, 'Документальный');
MERGE INTO d_genre KEY (id) VALUES (6, 'Боевик');