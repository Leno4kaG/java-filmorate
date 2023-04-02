package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    Map<Integer, Film> films = new HashMap<>();

    private static int id = 0;

    @Override
    public Optional<Film> save(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        if (films.get(film.getId()) == null) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return Optional.of(films.get(id));
    }

    @Override
    public List<Film> getListFilms(int count) {
        List<Film> allFilms = new ArrayList<>(films.values());
        if (allFilms.isEmpty()) {
            return allFilms;
        }
        return allFilms.stream().sorted(this::compare).limit(count).collect(Collectors.toList());
    }

    @Override
    public boolean addLike(int id, int userId) {
        Film film = films.get(id);
        film.addLike(userId);
        return true;
    }

    @Override
    public boolean deleteLike(int id, int userId) {
        Film film = films.get(id);
        film.deleteLike(userId);
        return true;
    }

    @Override
    public List<Genre> getGenres() {
        return null;
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        return Optional.empty();
    }

    @Override
    public List<Rating> getRatings() {
        return null;
    }

    @Override
    public Optional<Rating> getRating(int id) {
        return Optional.empty();
    }

    private int compare(Film f0, Film f1) {
        int size0 = f0.getSizeLikes();
        int size1 = f1.getSizeLikes();
        return Integer.compare(size1, size0);
    }
}
