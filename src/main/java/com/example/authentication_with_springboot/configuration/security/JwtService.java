package com.example.authentication_with_springboot.configuration.security;

import com.example.authentication_with_springboot.exception.RestAuthenticationEntryPoint;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    // secret key  jwt
    @Value("${jwt.token.secret}")
    private String secret;
    // expiration of token
    @Value("${jwt.token.expires}")
    private Long jwtExpiresMinutes;

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // generates a JWT token from  username
    public String generateToken(String username) {

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiresMinutes * 60 * 1000))
                // .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .signWith(getSignInKey())
                .compact();

        log.info("Generated JWT for user: {}", username);
        log.info("Secret used: {}", secret);
        log.info("Token: {}", token);

        return token;
    }


    // validates a JWT token
    public void validateToken(String token) throws JwtException {

        try {
            // claims are pieces of information stored inside a JWT. They describe things about the user or the token itself.
            Claims claims = Jwts.parser() // It will help decode and validate the JWT token
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token) //  Throws error if invalid or expired (this is required in v0.12+)
                    .getPayload();

            log.info("Token subject: {}", claims.getSubject());

        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
            // catch null, wrong token, expired token
            throw new JwtException(e.getMessage());
        }
    }

    // check if token is expired
    public boolean isTokenExpired(String token) {

        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // extracts username (subject) from JWT
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    // Extract all claims after signature verification
    private Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /* when your backend later validates the token, it uses the same key to check if the token was changed. If someone tries to modify the token, validation will fail.
       hmacShaKeyFor : It is used to safely create an HMAC  Key from a byte array to sign or verify JWTs using algorithms like HS256, HS384, or HS512.
     */
    private SecretKey getSignInKey() {

        // SignatureAlgorithm.HS256, this.secret
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        // Keys.hmacShaKeyFor to create a cryptographic key
        return Keys.hmacShaKeyFor(keyBytes);
        // It converts the string directly to bytes using UTF-8 encoding — no decoding
//       return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<String> extractTokenFromRequest(HttpServletRequest request) {

        // Get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        } else {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    // when user logout
    public void blacklistUser(String token) throws IOException {

        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        blacklist.put(token, expiration.toInstant());
    }

    public boolean isBlacklisted(String token) {

        return blacklist.containsKey(token);
    }

}