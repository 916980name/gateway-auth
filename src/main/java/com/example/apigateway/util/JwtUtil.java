package com.example.apigateway.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static String generateToken(KeyPair keyPair, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Claims claims = Jwts.claims();
        claims.setSubject(subject);
        claims.setIssuedAt(now);
        claims.setExpiration(new Date(nowMillis + 1000 * 60 * 60));

        // Create a JWT builder
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setClaims(claims);
        // jwtBuilder.signWith(secretKey);
        jwtBuilder.signWith(keyPair.getPrivate());

        // Generate the JWT token
        String token = jwtBuilder.compact();

        log.info("Generated JWT: {}", token);
        return token;
    }

    public static boolean verifyTokenExpired(KeyPair keyPair, String jwt) {
        try {
            return getAllClaimsFromToken(keyPair, jwt).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // https://blog.knoldus.com/spring-cloud-gateway-with-jwt/
    public static Claims getAllClaimsFromToken(KeyPair keyPair, String jwt) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private static SecretKey generateSecretKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
