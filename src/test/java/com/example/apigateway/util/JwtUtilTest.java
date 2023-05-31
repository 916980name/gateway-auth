package com.example.apigateway.util;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class JwtUtilTest {
   
   @Test
   void should_suc_when_verify_then_given_valid_token() throws NoSuchAlgorithmException {
        // given
        KeyPair keyPair = RSAUtils.generateKeyPair();
        String subject = "testsubject";
        String token = JwtUtil.generateToken(keyPair, subject);

        // when
        boolean result = JwtUtil.verifyTokenExpired(keyPair, token);

        // then
        assertFalse(result);
   } 
}
