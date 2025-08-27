package mtt.exmaplebackend.model.dto.response;

import java.util.List;

public record UsersResponse(
        List<UserResponse> users
) {
}
