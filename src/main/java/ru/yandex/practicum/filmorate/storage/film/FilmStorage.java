package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public Optional<Film> save(Film film);

    public Optional<Film> update(Film film);

    public List<Film> getFilms();

    public Optional<Film> getFilm(int id);

    List<Film> getListFilms(int count);

    boolean addLike(int id, int userId);

    boolean deleteLike(int id, int userId);

    List<Genre> getGenres();

    Optional<Genre> getGenre(int id);

    List<Rating> getRatings();

    Optional<Rating> getRating(int id);
}
