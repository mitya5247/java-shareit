package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class UnknownState extends Exception {

    String error;

    public UnknownState(String error) {
        this.error = error;
    }
}
