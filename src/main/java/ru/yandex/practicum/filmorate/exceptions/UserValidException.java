package ru.yandex.practicum.filmorate.exceptions;

public class UserValidException extends RuntimeException {
    public UserValidException() {
    }

    public UserValidException(String message) {
        super(message);
    }

    public UserValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserValidException(Throwable cause) {
        super(cause);
    }

    public UserValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
