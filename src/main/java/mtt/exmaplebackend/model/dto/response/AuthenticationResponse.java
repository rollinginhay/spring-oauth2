package mtt.exmaplebackend.model.dto.response;

import java.time.LocalDateTime;
import java.util.Collection;

public record AuthenticationResponse(
        String userId,
        String email,
        Collection<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String token
) {
}
