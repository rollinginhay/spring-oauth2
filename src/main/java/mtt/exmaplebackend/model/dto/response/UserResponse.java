package mtt.exmaplebackend.model.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserResponse(

        String email,

        String name,

        String phoneNumber,

        List<String> roles,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
