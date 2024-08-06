package ru.yandex.practicum.filmorate.exception;

public class DataConflictException extends RuntimeException{

    public DataConflictException(final String message) {
        super(message);
    }
}
