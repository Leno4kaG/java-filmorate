package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

    private Rating mpa;

    private Set<Genre> genres = new HashSet<>();
    private Set<Integer> likes = new HashSet<>();

    public void addLike(int id) {
        likes.add(id);
    }

    public boolean deleteLike(int id) {

        return likes.remove(id);
    }

    public int getSizeLikes() {
        return likes.size();
    }

    public void updateGenre(Set<Genre> newGenres){
        genres.clear();
        genres.addAll(newGenres);
    }

    public void deleteGenre(Genre genre){
        genres.remove(genre);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(likes, film.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, likes);
    }
}
