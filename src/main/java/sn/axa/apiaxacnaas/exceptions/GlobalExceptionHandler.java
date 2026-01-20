package sn.axa.apiaxacnaas.exceptions;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError
                .builder()
                        .status(404)
                        . code("RESOURCE_NOT_FOUND")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build());

    }
}
