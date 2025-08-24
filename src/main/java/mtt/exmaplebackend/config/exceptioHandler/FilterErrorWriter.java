package mtt.exmaplebackend.config.exceptioHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mtt.exmaplebackend.model.dto.response.ApiError;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FilterErrorWriter {
    private final ObjectMapper objectMapper;

    public void writeError(HttpServletResponse response, int status, String message, String description) throws IOException {
        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .details(description)
                .build();

        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errBody);
    }
}
