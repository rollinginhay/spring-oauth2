package mtt.exmaplebackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.model.dto.response.AuthenticationResponse;
import mtt.exmaplebackend.model.dto.response.UsersResponse;
import mtt.exmaplebackend.service.AuthService;
import mtt.exmaplebackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "Get all users, require authorized request", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "200", description = "Successfully authorized", content = @Content(schema = @Schema(implementation = UsersResponse.class)))
    @GetMapping("/users")
    public ResponseEntity<UsersResponse> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Add ADMIN to the authenticated user's roles", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "200", description = "Successfully elevated user to admin", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
    @GetMapping("/elevate")
    public ResponseEntity<AuthenticationResponse> elevate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String email = authentication.getName();


        return ResponseEntity.ok(authService.elevate(email));
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Any role is fine");
    }

    @Operation(summary = "Route requires both USER and ADMIN roles", security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponse(responseCode = "200", description = "Successfully accessed route")
    @GetMapping("/needtwo")
    public ResponseEntity<String> needTwo() {
        return ResponseEntity.ok("Successfully accessed route");
    }

}
