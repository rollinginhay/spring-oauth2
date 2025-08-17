package mtt.exmaplebackend.service;

import lombok.RequiredArgsConstructor;
import mtt.exmaplebackend.model.User;
import mtt.exmaplebackend.model.dto.response.UsersResponse;
import mtt.exmaplebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UsersResponse getAllUsers() {
        List<User> users = userRepository.findAll();

        return new UsersResponse(users);
    }
}
