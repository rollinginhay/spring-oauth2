package mtt.exmaplebackend.config.exceptioHandler;

import mtt.exmaplebackend.model.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Component
public class ApiErrorWriter {
    public ResponseEntity<ApiError> write(Exception ex, WebRequest request, HttpStatus status) {
        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errBody, status);
    }
}
