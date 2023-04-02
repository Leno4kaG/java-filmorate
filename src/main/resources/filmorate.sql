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
