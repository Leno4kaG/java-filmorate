package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> save(Film film);

    Optional<Film> update(Film film);

    List<Film> getFilms();

    Optional<Film> getFilm(int id);

    List<Film> getListFilms(int count);

    boolean addLike(Film film, int userId);

    boolean deleteLike(Film film, int userId);

    List<Genre> getGenres();

    Optional<Genre> getGenre(int id);

    List<Rating> getRatings();

    Optional<Rating> getRating(int id);
}
