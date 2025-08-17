package mtt.exmaplebackend.model.dto.response;

import java.time.LocalDateTime;

public record AuthenticationResponse(
        String userId,
        String email,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String token
) {
}
