package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(int id, int userId) {
        Film film = Optional.ofNullable(filmStorage.getFilm(id)).
                orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
        if (userId<0){
            throw new FilmNotFoundException(String.format("Id %d не должен быть меньше 0", userId));
        }
            film.addLike(userId);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        Film film = Optional.ofNullable(filmStorage.getFilm(id)).
                orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
        if (userId<0){
            throw new FilmNotFoundException(String.format("Id %d не должен быть меньше 0", userId));
        }
        film.deleteLike(userId);
        return film;
    }

    public List<Film> getListFilms(int count) {
        List<Film> allFilms = filmStorage.getFilms();

        if(allFilms.isEmpty()){
            return allFilms;
        }
        allFilms.sort(Collections.reverseOrder());
        return allFilms.stream().limit(count).collect(Collectors.toList());
    }
}
