package mtt.exmaplebackend.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.config.exceptioHandler.FilterErrorWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OauthSuccessHandler oauthSuccessHandler;
    private final OauthFailureHandler oauthFailureHandler;
    private final FilterErrorWriter filterErrorWriter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/**", "/api-docs/**", "swagger-ui/**", "/api/v1/auth/**", "/oauth2/**", "/login/oauth2/**", "/error", "swagger-ui.html").permitAll()
                        .requestMatchers("/api/v1/user/**").hasRole("USER")
                        .anyRequest().authenticated())
                /*
                 * Security filter exceptions are not caught by custom exceptionhandlers, have to be handled separately
                 * */
                .exceptionHandling(exHandler -> exHandler
                        //when unauthorized on specific routes
                        .defaultAuthenticationEntryPointFor((request, response, exception) -> {
                            log.error(exception.getMessage(), exception);
                            filterErrorWriter.writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access", request.getRequestURI());
                        }, request -> request.getRequestURI().startsWith("/api/"))
                        .accessDeniedHandler((request, response, exception) -> filterErrorWriter.writeError(response, HttpServletResponse.SC_FORBIDDEN, "Access denied", request.getRequestURI()))
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(redirect -> redirect.baseUri("/login/oauth2/code/*"))
                        .successHandler(oauthSuccessHandler)
                        .failureHandler(oauthFailureHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
