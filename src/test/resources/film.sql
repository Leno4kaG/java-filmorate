DROP TABLE IF EXISTS PUBLIC.LIKES;
DROP TABLE IF EXISTS PUBLIC.FRIENDS;
DROP TABLE IF EXISTS PUBLIC.GENRES;
DROP TABLE IF EXISTS PUBLIC.GENRE;
DROP TABLE IF EXISTS PUBLIC."USER";
DROP TABLE IF EXISTS PUBLIC.FILM;
DROP TABLE IF EXISTS PUBLIC.RATING;

CREATE TABLE PUBLIC.GENRE (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME VARCHAR_IGNORECASE,
	CONSTRAINT GENRE_PK PRIMARY KEY (ID)
);

INSERT INTO PUBLIC.GENRE (NAME) VALUES
	 ('Комедия'),
	 ('Драма'),
	 ('Мультфильм'),
	 ('Триллер'),
	 ('Документальный'),
	 ('Боевик');




CREATE TABLE PUBLIC.RATING (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME VARCHAR_IGNORECASE,
	CONSTRAINT RATING_PK PRIMARY KEY (ID)
);

INSERT INTO PUBLIC.RATING (NAME) VALUES
	 ('G'),
	 ('PG'),
	 ('PG-13'),
	 ('R'),
	 ('NC-17');



CREATE TABLE PUBLIC.FILM (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	RELEASE_DATE DATE,
	DURATION BIGINT,
	mpa INTEGER,
	NAME VARCHAR_IGNORECASE(70),
	DESCRIPTION VARCHAR_IGNORECASE(225),
	CONSTRAINT FILM_PK PRIMARY KEY (ID),
	CONSTRAINT FILM_FK FOREIGN KEY (MPA) REFERENCES PUBLIC.RATING(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE PUBLIC."USER" (
	BIRTHDAY DATE,
	EMAIL VARCHAR_IGNORECASE(30) NOT NULL,
	LOGIN VARCHAR_IGNORECASE(30),
	NAME VARCHAR_IGNORECASE(30),
	ID INTEGER NOT NULL AUTO_INCREMENT,
	CONSTRAINT USER_PK PRIMARY KEY (ID)
);


CREATE TABLE PUBLIC.LIKES (
	USER_ID INTEGER,
	FILM_ID INTEGER
);


CREATE TABLE PUBLIC.FRIENDS (
	USER_ID INTEGER,
	FRIEND_ID INTEGER,
	CONFIRMATION_STATUS BOOLEAN
);



CREATE TABLE PUBLIC.GENRES (
	FILM_ID INTEGER,
	GENRE_ID INTEGER,
	CONSTRAINT GENRES_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT GENRES_FK_1 FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO PUBLIC.FILM (RELEASE_DATE,DURATION,MPA,NAME,DESCRIPTION) VALUES
	 ('1899-12-09',45, 1, 'name','Description'),
	 ('2014-04-05',112, 1,'1+1','Пострадав в результате несчастного случая, богатый аристократ Филипп нанимает в помощники человека, который менее всего подходит для этой работы, – молодого жителя предместья Дрисса, только что освободившегося из тюрьмы.'),
	 ('2022-07-03',113, 2,'Джентльмены','Наркобарон хочет уйти на покой, но криминальный мир не отпускает. Успешное возвращение Гая Ричи к корням');

INSERT INTO PUBLIC."USER" (BIRTHDAY,EMAIL,LOGIN,NAME) VALUES
('1976-09-20','mail@yandex.ru','doloreUpdate','est adipisicing'),
	 ('2004-04-08','anna@mail.ru','anna13','Anna'),
	 ('2008-09-06','alex@yandex.ru','alex45','alex'),
	 ('1988-02-10','elena@yandex.ru','elena4','elena');

INSERT INTO PUBLIC.LIKES (USER_ID,FILM_ID) VALUES
	 (1,1),
	 (1,2),
	 (2,2),
	 (3,2);
INSERT INTO PUBLIC.FRIENDS (USER_ID,FRIEND_ID,CONFIRMATION_STATUS) VALUES
	 (1,2,true),
	 (2,3,true),
	 (4,2,false);

INSERT INTO PUBLIC.GENRES (FILM_ID,GENRE_ID) VALUES
	 (2,1),
	 (2,2);

