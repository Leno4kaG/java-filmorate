package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class Film {

    private int id;

    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
}
