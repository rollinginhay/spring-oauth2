package mtt.exmaplebackend.service;

import lombok.RequiredArgsConstructor;
import mtt.exmaplebackend.model.Role;
import mtt.exmaplebackend.model.User;
import mtt.exmaplebackend.model.dto.response.UserResponse;
import mtt.exmaplebackend.model.dto.response.UsersResponse;
import mtt.exmaplebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UsersResponse getAllUsers() {
        List<User> users = userRepository.findAll();


        return new UsersResponse(
                users.stream()
                        .map(user -> UserResponse
                                .builder()
                                .email(user.getEmail())
                                .name(user.getName())
                                .phoneNumber(user.getPhoneNumber())
                                .createdAt(user.getCreatedAt())
                                .updatedAt(user.getUpdatedAt())
                                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList())
        );
    }
}
