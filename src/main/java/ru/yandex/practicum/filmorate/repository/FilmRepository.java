package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmRepository {
    Map<Integer, Film> films = new HashMap<>();

    private static int id = 0;

    public Film save(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (films.get(film.getId()) == null) {
            throw new ValidationException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
