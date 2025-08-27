package mtt.exmaplebackend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import mtt.exmaplebackend.config.exceptioHandler.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtService {
    private final String secret;
    private final long expiration;
    private final JwtParser jwtParser;

    public JwtService(@Value("${app.security.jwt.secret}") String secret,
                      @Value("${app.security.jwt.expiration}") long expiration) {
        this.secret = secret;
        this.expiration = expiration;
        this.jwtParser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build();
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        log.info("Generating JWT token for subject: {}", subject);
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(Keys.hmacShaKeyFor((secret.getBytes())))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = jwtParser.parseSignedClaims(token);

            return true;
        } catch (SignatureException e) {
            throw new UnauthorizedException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("JWT claims string is empty");
        } catch (Exception e) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

}
