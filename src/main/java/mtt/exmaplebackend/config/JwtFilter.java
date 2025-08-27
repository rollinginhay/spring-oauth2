package mtt.exmaplebackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.model.Role;
import mtt.exmaplebackend.model.User;
import mtt.exmaplebackend.repository.UserRepository;
import mtt.exmaplebackend.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");


        //pass along if no valid header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (jwtService.validateToken(token)) {
            String email = jwtService.extractSubject(token);
            User user = userRepository.findByEmail(email).orElseThrow();

            //remember: get roles from db not token
            List<String> roles = user.getRoles().stream().map(Role::getName).toList();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, roles.stream().map(SimpleGrantedAuthority::new).toList());

            //adds sess id and req ip to authtoken metadata
            authToken.setDetails(new WebAuthenticationDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        }

    }

}
