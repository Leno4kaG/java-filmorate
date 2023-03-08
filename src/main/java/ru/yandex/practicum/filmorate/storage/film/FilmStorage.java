package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film save(Film film);

    public Film update(Film film);

    public List<Film> getFilms();

    public Film getFilm(int id);
}
