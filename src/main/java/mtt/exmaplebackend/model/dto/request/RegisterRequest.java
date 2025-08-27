package mtt.exmaplebackend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import mtt.exmaplebackend.util.validation.ValidPassword;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @ValidPassword(message = "Password must contain at least 8 characters, an uppercase character, a number, and a special character")
        String password,

        @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Invalid phone number")
        String phoneNumber
) {
}
