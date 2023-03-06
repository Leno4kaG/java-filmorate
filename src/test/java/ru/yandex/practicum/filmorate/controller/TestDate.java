package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestDate {

    public static String USER_NULL = "User не должен быть пустым";

    public static String EMAIL_ERROR = "Электронная почта не должна быть пустой";

    public static String LOGIN_ERROR = "Логин не должен быть пустым";

    public static String BIRTHDAY_ERROR = "Дата рождения не может быть в будущем";

    public static String FILM_ERROR = "Фильм не должен быть пустым";

    public static String FILM_NAME_ERROR = "Название фильма не должно быть пустым";

    public static String DESCRIPTION_ERROR = "Максимальная длина описания - 200 символов";

    public static String RELEASE_DATE = "Дата релиза не раньше 28.12.1895";

    public static String DURATION_ERROR = "Продолжительность фильма должна быть положительной";

    public static User addUser(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("login");
        user.setName("");
        user.setBirthday(LocalDate.parse("1995-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return user;
    }
    public static Film getFilm(){
        Film film = new Film();
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.parse("1899-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        film.setDuration(35);
        return film;
    }
}
