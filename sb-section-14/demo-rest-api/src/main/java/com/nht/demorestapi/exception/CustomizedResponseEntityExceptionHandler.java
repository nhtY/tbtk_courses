package com.nht.demorestapi.exception;

import com.nht.demorestapi.user.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

// Bu haliyle controller'lara girmeden önceki error'lar yakalanmıyor. Mesela /someNonExistentUrl'e istek atarsan
// {
//  "type": "about:blank",
//  "title": "Not Found",
//  "status": 404,
//  "detail": "No static resource nonExistentUrl.",
//  "instance": "/nonExistentUrl"
//}
// Bu tarz error'lar için de customize etmeyi öğren.
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleuserNotFoundExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger errNo = new AtomicInteger();

        ex.getFieldErrors()
                .forEach(err -> {
                    stringBuilder.append("[").append(errNo.incrementAndGet()).append("]");
                    stringBuilder.append(err.getDefaultMessage());
                    stringBuilder.append(errNo.get() < ex.getErrorCount() ? '\n' : "");
                });

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                stringBuilder.toString(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
