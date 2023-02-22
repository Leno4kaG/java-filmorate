package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {


    private final ValidateService validateService;
    private final FilmRepository repository;

    @PostMapping
    public Film addFilm(@RequestBody Film newFilm) {
        validateService.validateFilm(newFilm);
        Film filmSave = repository.save(newFilm);
        log.info("Добавлен фильм = {}", newFilm.getName());
        return filmSave;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateService.validateFilm(film);
        Film filmUpdate = repository.update(film);
        log.info("Фильм с ID = "+film.getId()+" обновлен");

        return filmUpdate;
    }

    @GetMapping
    public List<Film> getFilms() {
        return repository.getFilms();
    }
}
