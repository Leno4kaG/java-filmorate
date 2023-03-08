package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film newFilm) {
        ValidateService.validateFilm(newFilm);
        Film filmSave = filmStorage.save(newFilm);
        log.info("Добавлен фильм = {}", newFilm.getName());
        return filmSave;
    }
    public Film updateFilm(Film film) {
       ValidateService.validateFilm(film);
        Film filmUpdate = filmStorage.update(film);
        log.info("Фильм обновлен Id = {}", film.getId());

        return filmUpdate;
    }
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }
    public Film getFilmById(Integer id) {
        return Optional.ofNullable(filmStorage.getFilm(id)).orElseThrow(() ->
                new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
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
        return allFilms.stream().sorted(this::compare).limit(count).collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        int size0 = f0.getSizeLikes();
        int size1 = f1.getSizeLikes();
        return Integer.compare(size1, size0);
    }
}
