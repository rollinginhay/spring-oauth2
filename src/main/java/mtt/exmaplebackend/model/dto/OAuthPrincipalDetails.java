package mtt.exmaplebackend.model.dto;

public record OAuthPrincipalDetails(
        String email,
        String name,
        String subject
) {
}
