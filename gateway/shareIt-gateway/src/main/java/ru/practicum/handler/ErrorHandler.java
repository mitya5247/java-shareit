package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.BookingDtoIsNotValidException;
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
    public UnknownStateException handleItemNotFound(final UnknownStateException exception) {
        log.info(exception.getMessage());
        return exception;
    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleItemNotFound(final StackOverflowError exception) {
//        log.info("stack over flow");
//        log.info(exception.getMessage());
//        return new ErrorResponse(exception.getMessage());
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleBadComment(final BadCommentException exception) {
//        log.info(exception.getMessage());
//        return new ErrorResponse(exception.getMessage());
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleBadComment(final NotEmptyDescriptionException exception) {
//        log.info(exception.getMessage());
//        return new ErrorResponse(exception.getMessage());
//    }

}
