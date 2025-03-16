package com.learning.userservice.userservice.security;

import com.learning.userservice.userservice.model.UserAccount;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenValidityInMilliseconds;

    // Generate access token
    public String generateAccessToken(UserAccount user) {
        return generateToken(user.getEmail(), accessTokenValidityInMilliseconds);
    }

    // Generate refresh token
    public String generateRefreshToken(UserAccount user) {
        return generateToken(user.getEmail(), refreshTokenValidityInMilliseconds);
    }

    // Validate general JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.info("JWT signature không hợp lệ: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.info("Token không đúng định dạng: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("Token đã hết hạn: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.info("Token không được hỗ trợ: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.info("Token rỗng hoặc không hợp lệ: {}", ex.getMessage());
        }
        return false;
    }

    // Get expiration date from token
    public Instant getExpirationDateFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        var claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().toInstant();
    }

    // Generate token method used by both access and refresh token generation
    private String generateToken(String userName, long expirationMs) {
        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512).build();
            var now = Instant.now();
            var expirationTime = now.plusMillis(expirationMs);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(userName)
                    .issuer("OnlineMarketplace")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expirationTime))
                    .claim("role", "USER")
                    .build();

            var payload = new Payload(jwtClaimsSet.toJSONObject());
            var jwsObject = new JWSObject(header, payload);

            jwsObject.sign(new MACSigner(secretKey));

            return jwsObject.serialize();
        }
        catch (Exception e) {
            log.error("Failed to generate token", e);
            throw new RuntimeException("Failed to generate token with message: " + e.getMessage());
        }
    }

    // Validate refresh token
    public boolean validateRefreshToken(String refreshToken) {
        try {
            // Use JJWT library to parse and validate the refresh token
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(refreshToken); // If the token is invalid or expired, an exception is thrown
            return true; // Return true if validation is successful
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException ex) {
            log.error("Invalid refresh token: {}", ex.getMessage());
            return false; // Return false if validation fails
        }
    }

    // Get the user ID (or subject) from the refresh token
    public String getUserIdFromToken(String refreshToken) {
        try {
            // Extract the claims from the refresh token
            var claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Return the subject (user email or user ID from claims)
            return claims.getSubject();
        } catch (JwtException ex) {
            log.error("Failed to extract user ID from token: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired refresh token");
        }
    }
}
