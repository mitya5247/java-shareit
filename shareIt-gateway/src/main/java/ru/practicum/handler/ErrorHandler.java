package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.BookingDtoIsNotValidException;
import ru.practicum.exceptions.NotEmptyDescriptionException;
import ru.practicum.exceptions.UnknownStateException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDtoNotValid(final BookingDtoIsNotValidException exception) {
        log.info(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UnknownStateException handleUnknownState(final UnknownStateException exception) {
        log.info(exception.getMessage());
        return exception;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadComment(final NotEmptyDescriptionException exception) {
        log.info(exception.getMessage());
       return new ErrorResponse(exception.getMessage());
    }

}
