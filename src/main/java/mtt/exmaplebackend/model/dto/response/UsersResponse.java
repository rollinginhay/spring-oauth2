package mtt.exmaplebackend.model.dto.response;

import mtt.exmaplebackend.model.User;

import java.util.List;

public record UsersResponse(
        List<User> users
) {
}
