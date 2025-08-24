package mtt.exmaplebackend.config.exceptioHandler;

import lombok.RequiredArgsConstructor;
import mtt.exmaplebackend.model.dto.response.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ApiErrorWriter apiErrorWriter;

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ApiError> handleUnauthorizedException(Exception ex, WebRequest request) {
        return apiErrorWriter.write(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestexception.class)
    public final ResponseEntity<ApiError> handleBadRequestException(Exception ex, WebRequest request) {
        return apiErrorWriter.write(ex, request, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleAnyException(Exception ex, WebRequest request) {

        return apiErrorWriter.write(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
