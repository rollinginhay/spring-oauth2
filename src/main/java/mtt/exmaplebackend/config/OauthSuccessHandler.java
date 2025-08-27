package mtt.exmaplebackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.model.Role;
import mtt.exmaplebackend.model.RoleDef;
import mtt.exmaplebackend.model.User;
import mtt.exmaplebackend.model.dto.OAuthPrincipalDetails;
import mtt.exmaplebackend.model.dto.response.AuthenticationResponse;
import mtt.exmaplebackend.repository.RoleRepository;
import mtt.exmaplebackend.repository.UserRepository;
import mtt.exmaplebackend.service.JwtService;
import mtt.exmaplebackend.util.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        //extract principal
        OAuthPrincipalDetails principalDetails = getPrincipalDetails(oauthToken.getPrincipal());
        String email = principalDetails.email();
        String name = principalDetails.name();
        String subject = principalDetails.subject();

        //Save new user or update existing
        User user = userRepository.findByEmail(email)
                //exists
                .map(existingUser -> {
                    //if user is not oauth registered
                    if (!existingUser.getIsOauth2User()) {
                        existingUser.setIsOauth2User(true);
                        existingUser.setOauth2Provider("google");
                        existingUser.setOauth2Id(subject);
                        existingUser.setName(name);
                        existingUser.setUpdatedAt(LocalDateTime.now());
                    }
                    return userRepository.save(existingUser);
                })
                //new user
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .oauth2Id(subject)
                            .isOauth2User(true)
                            .roles(List.of(roleRepository.findByName(RoleDef.ROLE_USER.name()).get()))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(newUser);
                });

        //generate jwt token
        String jwtToken = jwtService.generateToken(
                user.getEmail(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
        );

        log.warn("On success handler, claims: {}",
                Map.of("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
        );

        //return auth resp
        AuthenticationResponse authResp = UserMapper.mapToAuthResponse(user, jwtToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), authResp);
        //res req automatically passed next
    }

    private OAuthPrincipalDetails getPrincipalDetails(OAuth2User principal) {

        String email;
        String name;
        String subject;

        email = Objects.requireNonNull(principal.getAttribute("email"), "Email cannot be null");
        name = Objects.requireNonNull(principal.getAttribute("name"), "Name cannot be null");
        subject = Objects.requireNonNull(principal.getName(), "Subject cannot be null");
        return new OAuthPrincipalDetails(email, name, subject);
    }
}
