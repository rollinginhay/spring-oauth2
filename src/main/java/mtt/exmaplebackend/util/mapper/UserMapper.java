package mtt.exmaplebackend.util.mapper;

import mtt.exmaplebackend.model.Role;
import mtt.exmaplebackend.model.User;
import mtt.exmaplebackend.model.dto.response.AuthenticationResponse;

import java.util.stream.Collectors;

public class UserMapper {
    public static AuthenticationResponse mapToAuthResponse(User user, String jwtToken) {
        return new AuthenticationResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                jwtToken
        );
    }
}
