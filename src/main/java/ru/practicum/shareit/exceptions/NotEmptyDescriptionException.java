package ru.practicum.shareit.exceptions;

import javax.validation.Payload;

public class NotEmptyDescriptionException extends Exception implements Payload {
    public NotEmptyDescriptionException(String message) {
        super(message);
    }
}
