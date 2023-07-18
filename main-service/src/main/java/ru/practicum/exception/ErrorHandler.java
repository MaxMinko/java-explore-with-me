package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFoundException(final CategoryNotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRequestNotFoundException(final RequestNotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRepeatRequestException(final RepeatRequestException e) {
        log.debug("Получен статус 409 CONFLICT {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEventNotFoundException(final EventNotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventValidationException(final EventValidationException e) {
        log.debug("Получен статус 409 CONFLICT {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTimeValidationException(final TimeValidationException e) {
        log.debug("Получен статус 400 BAD_REQUEST {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlePsqlException(final PSQLException e) {
        log.debug("Получен статус 409 CONFLICT {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCompilationNotFoundException(final CompilationNotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRequestValidationException(final RequestValidationException e) {
        log.debug("Получен статус 409 CONFLICT {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryValidationException(final CategoryValidationException e) {
        log.debug("Получен статус 409 CONFLICT {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCommentNotFoundException(final CommentNotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCommentException(final CommentException e) {
        log.debug("Получен статус 409 CONFLICT {}",e.getMessage(),e);
        return new ErrorResponse(e.getMessage());
    }
}
