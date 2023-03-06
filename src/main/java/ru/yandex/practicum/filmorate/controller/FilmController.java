package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {


    private final ValidateService validateService;
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@RequestBody Film newFilm) {
        validateService.validateFilm(newFilm);
        Film filmSave = filmStorage.save(newFilm);
        log.info("Добавлен фильм = {}", newFilm.getName());
        return filmSave;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateService.validateFilm(film);
        Film filmUpdate = filmStorage.update(film);
        log.info("Фильм с ID = " + film.getId() + " обновлен");

        return filmUpdate;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return Optional.ofNullable(filmStorage.getFilm(id)).orElseThrow(() ->
                new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getListFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Count < 0");
        }
        return filmService.getListFilms(count);
    }

}
