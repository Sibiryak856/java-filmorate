package ru.yandex.practicum.filmorate.exception;

public class IncorrectParameterException extends RuntimeException {
    public IncorrectParameterException(String action) {
    }

    public IncorrectParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectParameterException(Throwable cause) {
        super(cause);
    }

    public IncorrectParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IncorrectParameterException() {
    }
}
