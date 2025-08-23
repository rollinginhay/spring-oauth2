package mtt.exmaplebackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String oauth2Provider;

    private String oauth2Id;

    private Boolean isOauth2User = false;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Role> roles;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

}