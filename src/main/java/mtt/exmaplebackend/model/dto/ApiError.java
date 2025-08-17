package mtt.exmaplebackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ApiError {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
