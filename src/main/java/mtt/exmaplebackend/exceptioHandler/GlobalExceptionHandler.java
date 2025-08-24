package mtt.exmaplebackend.exceptioHandler;

import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.model.dto.ApiError;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
@Order(-1)
@EnableWebMvc
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleAnyException(Exception ex, WebRequest request) {
        log.warn("see if this actaully triggers");

        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ApiError> handleUnauthorizedException(Exception ex, WebRequest request) {
        log.warn("see if this actaully triggers");
        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errBody, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestexception.class)
    public final ResponseEntity<ApiError> handleBadRequestException(Exception ex, WebRequest request) {
        log.warn("see if this actaully triggers");
        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errBody, HttpStatus.BAD_REQUEST);
    }

}
