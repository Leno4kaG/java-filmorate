package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.controller.TestDate.*;

class FilmControllerTest {


    private ValidateService validateService = new ValidateService();
    private InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    private FilmService filmService = new FilmService(filmStorage);

    private FilmController filmController = new FilmController(filmService);


    @Test
    void addFilmIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(null));
        assertEquals(FILM_ERROR, exception.getMessage());
    }

    @Test
    void addFilmIsNameEmptyOrNull() {
        Film film = new Film();
        film.setName(" ");
        Film film1 = new Film();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(FILM_NAME_ERROR, exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertEquals(FILM_NAME_ERROR, exception1.getMessage());
    }

    @Test
    void addFilmLengthDescription() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("Создайте заготовку проекта с помощью Spring Initializr. Некоторые параметры вы найдёте в этой таблице," +
                " остальные заполните самостоятельно. Ура! Проект сгенерирован. Теперь можно шаг за шагом реализовать приложение.");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(DESCRIPTION_ERROR, exception.getMessage());

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
        assertEquals(RELEASE_DATE, exception.getMessage());

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
        assertEquals(DURATION_ERROR, exception.getMessage());
    }

    @Test
    void updateFilmIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(null));
        assertEquals(FILM_ERROR, exception.getMessage());
    }

    @Test
    void updateFilmIsNameEmptyOrNull() {
        Film film = new Film();
        film.setName(" ");
        Film film1 = new Film();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals(FILM_NAME_ERROR, exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> filmController.updateFilm(film1));
        assertEquals(FILM_NAME_ERROR, exception1.getMessage());
    }

    @Test
    void updateFilmLengthDescription() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("Создайте заготовку проекта с помощью Spring Initializr. Некоторые параметры вы найдёте в этой таблице," +
                " остальные заполните самостоятельно. Ура! Проект сгенерирован. Теперь можно шаг за шагом реализовать приложение.");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals(DESCRIPTION_ERROR, exception.getMessage());

    }
    @Test
    void updateFilmReleaseDateIsAfter(){
        Film film = new Film();
        film.setName("name");
        film.setDescription("Создайте заготовку проекта с помощью Spring Initializr. Некоторые параметры вы найдёте в этой таблице, " +
                "остальные заполните самостоятельно.Ура! Проект сгенерирован. Теперь можно шаг за шагом реализован");
        film.setReleaseDate(LocalDate.parse("1895-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals(RELEASE_DATE, exception.getMessage());

    }
    @Test
    void updateFilmDurationNegative(){
        Film film = new Film();
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.parse("1899-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        film.setDuration(-1);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals(DURATION_ERROR, exception.getMessage());
    }
@Test
    void addLike(){
        Film film = filmController.addFilm(TestDate.getFilm());
        Film filmWithLike = filmController.addLike(film.getId(), 1);
        assertEquals(1, filmWithLike.getLikes().iterator().next());

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, ()-> filmController.addLike(film.getId(), -1));
        assertEquals("Id -1 не должен быть меньше 0", exception.getMessage());
    }
    @Test
    void deleteLike(){
        Film film = filmController.addFilm(TestDate.getFilm());
        Film filmWithLike = filmController.deleteLike(film.getId(), 1);
       assertTrue(filmWithLike.getLikes().isEmpty());

        FilmNotFoundException exception = assertThrows(FilmNotFoundException.class, ()-> filmController.deleteLike(film.getId(), -1));
        assertEquals("Id -1 не должен быть меньше 0", exception.getMessage());
    }
    @Test
    void getListFilms(){
        Film film = filmController.addFilm(TestDate.getFilm());
        Film filmWithLike = filmController.addLike(film.getId(), 1);

        List<Film> films = filmController.getListFilms(1);
        assertEquals(filmWithLike, films.get(0));

        ValidationException exception = assertThrows(ValidationException.class, ()-> filmController.getListFilms(-1));
        assertEquals("Count <= 0", exception.getMessage());

    }
}