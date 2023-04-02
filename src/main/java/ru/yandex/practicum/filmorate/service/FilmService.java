package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDaoImpl") FilmStorage filmStorage, @Qualifier("userDaoImpl") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film newFilm) {
        ValidateService.validateFilm(newFilm);
        Film filmSave = filmStorage.save(newFilm).orElseThrow(() ->
                new FilmNotFoundException(String.format("Фильм с ID %d не сохранен!!!", newFilm.getId())));
        log.info("Добавлен фильм = {}", newFilm.getName());
        return filmSave;
    }

    public Film updateFilm(Film film) {
        ValidateService.validateFilm(film);
        Film filmUpdate = filmStorage.update(film).orElseThrow(() ->
                new FilmNotFoundException(String.format("Фильм с ID %d не обновлен!!!", film.getId())));
        log.info("Фильм обновлен Id = {}", film.getId());

        return filmUpdate;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilm(id).orElseThrow(() ->
                new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
    }

    public Film addLike(int id, int userId) {
        Film film = filmStorage.getFilm(id).
                orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
        userStorage.getUser(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("Пользователь с идентификатором %d не найден.", userId)));
        filmStorage.addLike(id, userId);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        Film film = filmStorage.getFilm(id).
                orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
        userStorage.getUser(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("Пользователь с идентификатором %d не найден.", userId)));
        filmStorage.deleteLike(id, userId);
        return film;
    }

    public List<Film> getListFilms(int count) {
        return filmStorage.getListFilms(count);
    }

    public List<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id).orElseThrow(() -> new GenreNotFoundException(
                String.format("Жанр с id %d не найден ", id)));
    }

    public List<Rating> getRatings() {
        return filmStorage.getRatings();
    }

    public Rating getRating(int id) {
        return filmStorage.getRating(id).orElseThrow(() -> new RatingNotFoundException(
                String.format("Рейтинг с id %d не найден ", id)));
    }


}
