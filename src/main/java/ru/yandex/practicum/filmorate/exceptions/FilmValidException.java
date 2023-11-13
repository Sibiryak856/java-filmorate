package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidException extends RuntimeException {

    public FilmValidException() {
    }

    public FilmValidException(String message) {
        super(message);
    }

    public FilmValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilmValidException(Throwable cause) {
        super(cause);
    }

    public FilmValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
