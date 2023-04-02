package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class FilmController {

    private final FilmService filmService;

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film newFilm) {

        return filmService.addFilm(newFilm);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {


        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getListFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Count <= 0");
        }
        return filmService.getListFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres(){
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id){
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<Rating> getRatings(){
        return filmService.getRatings();
    }

    @GetMapping("/mpa/{id}")
    public Rating getRating(@PathVariable int id){
        return filmService.getRating(id);
    }

}
