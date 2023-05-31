package com.example.apigateway.config;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.apigateway.util.RSAUtils;


@Configuration
public class StaticKeyPairConfig {
    @Value("${key.private}")
    private String privateKeyStr;
    @Value("${key.public}")
    private String publicKeyStr;

    @Bean("jwtKeyPair")
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new KeyPair(RSAUtils.getPublicKey(publicKeyStr), RSAUtils.getPrivateKey(privateKeyStr));
    }
    
}
