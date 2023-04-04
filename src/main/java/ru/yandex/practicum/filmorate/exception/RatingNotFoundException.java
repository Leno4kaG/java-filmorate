package ru.yandex.practicum.filmorate.exception;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException() {
    }

    public RatingNotFoundException(String message) {
        super(message);
    }

    public RatingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
