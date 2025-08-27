package mtt.exmaplebackend.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mtt.exmaplebackend.config.exceptioHandler.ApiErrorWriter;
import mtt.exmaplebackend.model.dto.response.ApiError;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Requests from browser has Accept:text/html header prompts rendering html. Custom error mapping forces json response
 */
@RestController
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {
    private final ApiErrorWriter apiErrorWriter;

    @Hidden
    @GetMapping("/error")
    public ResponseEntity<Object> error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int statusCode;
        try {
            statusCode = Integer.parseInt(status.toString());
        } catch (Exception e) {
            statusCode = 500;
        }

        //Tomcat doesnt always populate error message
        String messageText = message.toString().isEmpty() ? HttpStatus.valueOf(statusCode).getReasonPhrase() : message.toString();

        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(messageText)
                .details(path.toString())
                .build();
        return new ResponseEntity<>(errBody, HttpStatusCode.valueOf(statusCode));

    }
}
