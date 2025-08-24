package mtt.exmaplebackend.model.dto.response;

import mtt.exmaplebackend.model.Role;

import java.time.LocalDateTime;
import java.util.Collection;

public record AuthenticationResponse(
        String userId,
        String email,
        Collection<Role> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String token
) {
}
