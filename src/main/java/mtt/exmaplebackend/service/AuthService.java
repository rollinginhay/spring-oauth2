package mtt.exmaplebackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.config.exceptioHandler.BadRequestexception;
import mtt.exmaplebackend.config.exceptioHandler.UnauthorizedException;
import mtt.exmaplebackend.model.Role;
import mtt.exmaplebackend.model.RoleDef;
import mtt.exmaplebackend.model.User;
import mtt.exmaplebackend.model.dto.request.LoginRequest;
import mtt.exmaplebackend.model.dto.request.RegisterRequest;
import mtt.exmaplebackend.model.dto.response.AuthenticationResponse;
import mtt.exmaplebackend.repository.RoleRepository;
import mtt.exmaplebackend.repository.UserRepository;
import mtt.exmaplebackend.util.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRoles()));

        return UserMapper.mapToAuthResponse(user, token);
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestexception("Email already in use");
        }
        User newUser = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .roles(List.of(roleRepository.findByName(RoleDef.ROLE_USER.name()).get()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = userRepository.save(newUser);

        String token = jwtService.generateToken(user.getEmail(), Map.of("roles", user.getRoles()));

        return UserMapper.mapToAuthResponse(user, token);
    }

    @Transactional
    public AuthenticationResponse elevate(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow();

            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

            if (!user.getRoles().contains(adminRole)) {
                user.getRoles().add(adminRole);
            }

            User elevated = userRepository.save(user);

            String token = jwtService.generateToken(elevated.getEmail(), Map.of("roles", user.getRoles().stream().map(Role::getName).toList()));

            return UserMapper.mapToAuthResponse(user, token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
