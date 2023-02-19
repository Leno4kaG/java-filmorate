package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {


    private ValidateService validateService = new ValidateService();
    private FilmRepository filmRepository = new FilmRepository();

    private FilmController filmController = new FilmController(validateService, filmRepository);


    @Test
    void addFilmIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(null));
        assertEquals("Фильм не должен быть пустым", exception.getMessage());
    }

    @Test
    void addFilmIsNameEmptyOrNull() {
        Film film = new Film();
        film.setName(" ");
        Film film1 = new Film();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Название фильма не должно быть пустым", exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertEquals("Название фильма не должно быть пустым", exception1.getMessage());
    }

    @Test
    void addFilmLengthDescription() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("Создайте заготовку проекта с помощью Spring Initializr. Некоторые параметры вы найдёте в этой таблице," +
                " остальные заполните самостоятельно. Ура! Проект сгенерирован. Теперь можно шаг за шагом реализовать приложение.");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Максимальная длина описания - 200 символов", exception.getMessage());

    }
    @Test
    void addFilmReleaseDateIsAfter(){
        Film film = new Film();
        film.setName("name");
        film.setDescription("Создайте заготовку проекта с помощью Spring Initializr. Некоторые параметры вы найдёте в этой таблице, " +
                "остальные заполните самостоятельно.Ура! Проект сгенерирован. Теперь можно шаг за шагом реализован");
        film.setReleaseDate(LocalDate.parse("1895-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Дата релиза не раньше 28.12.1895", exception.getMessage());

    }
    @Test
    void addFilmDurationNegative(){
        Film film = new Film();
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.parse("1899-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        film.setDuration(-1);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }
}