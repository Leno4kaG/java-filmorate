package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

    private Rating mpa;

    private List<Genre> genres = new ArrayList<>();
    private List<Integer> likes = new ArrayList<>();

    public void addLike(Integer id) {
        likes.add(id);
    }

    public boolean deleteLike(Integer id) {
        if (likes.isEmpty()) {
            return false;
        }
        return likes.remove(id);
    }

    public int getSizeLikes() {
        return likes.size();
    }

    public void updateGenre(Set<Genre> newGenres) {
        genres.clear();
        genres.addAll(newGenres);
    }
}
