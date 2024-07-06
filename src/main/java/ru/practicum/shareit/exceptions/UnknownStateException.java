package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class UnknownStateException extends Exception {

    String error;

    public UnknownStateException(String error) {
        this.error = error;
    }
}
