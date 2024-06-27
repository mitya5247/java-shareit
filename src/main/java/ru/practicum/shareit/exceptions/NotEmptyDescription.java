package ru.practicum.shareit.exceptions;

import javax.validation.Payload;

public class NotEmptyDescription extends Exception implements Payload {
    public NotEmptyDescription(String message) {
        super(message);
    }
}
