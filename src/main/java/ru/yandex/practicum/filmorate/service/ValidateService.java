package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Slf4j
@Component
public class ValidateService {

    public static final LocalDate DATE_RELEASE = LocalDate.parse("1895-12-28",
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public void validateUser(User user){

        if(user == null) {
            log.error("User не должен быть пустым");
            throw new ValidationException("User не должен быть пустым");
        }
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")){
            log.error("Электронная почта не должна быть пустой. email = {}", user.getEmail());
            throw new ValidationException("Электронная почта не должна быть пустой");
        }
        if(user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            log.error("Логин не должен быть пустым. login = {}", user.getLogin());
            throw new ValidationException("Логин не должен быть пустым");
        }
        if(user.getName() == null || user.getName().isBlank()){
            log.info("Имя пустое, name = {}", user.getName());
            user.setName(user.getLogin());
        }
        if(user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())){
            log.error("Дата рождения не может быть в будущем, date = {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

    }

    public void validateFilm(Film film){
        if (film == null) {
            log.error("Фильм не должен быть пустым");
            throw new ValidationException("Фильм не должен быть пустым");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название фильма не должно быть пустым, film {}", film.getName());
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов, description = {}", film.getDescription());
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(DATE_RELEASE)) {
            log.error("Дата релиза не раньше 28.12.1895, date release = {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной, duration {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

    }
}
