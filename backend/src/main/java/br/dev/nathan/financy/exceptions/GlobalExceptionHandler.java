package br.dev.nathan.financy.exceptions;

import br.dev.nathan.financy.dtos.response.error.ErrorResponse;
import br.dev.nathan.financy.dtos.response.error.BodyErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceOwnershipException.class)
    private ResponseEntity<BodyErrorResponse> handleResourceOwnershipException(ResourceOwnershipException exception,
                                                                               HttpServletRequest request) {

        BodyErrorResponse response = new BodyErrorResponse(
            404,
            "Not Found",
            List.of(),
            request.getRequestURI(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<BodyErrorResponse> handleValidationException(MethodArgumentNotValidException exception,
                                                                        HttpServletRequest request) {

        List<ErrorResponse> errors = exception.getFieldErrors()
            .stream()
            .map(error -> new ErrorResponse(
                error.getField(),
                error.getDefaultMessage()
            ))
            .toList();

        BodyErrorResponse response = new BodyErrorResponse(
            400,
            "Bad Request",
            errors,
            request.getRequestURI(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
