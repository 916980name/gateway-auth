package com.example.apigateway.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @Date 2022-07-02 17:18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RSAUtils {
    public static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM_SHA256 = "SHA256withRSA";
    public static final String SIGN_ALGORITHM_SHA1 = "SHA1withRSA";
    public static final int KEY_LENGTH_2048 = 2048;
    public static final int KEY_LENGTH_1024 = 1024;
    public static final int KEY_LENGTH_512 = 512;

    public static final String SIGN_ALGORITHM = SIGN_ALGORITHM_SHA1;
    public static final int KEY_LENGTH = KEY_LENGTH_2048;

    public static PrivateKey getPrivateKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        final byte[] bytes = Base64.decode(keyStr);
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        final byte[] bytes = Base64.decode(keyStr);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static String sign(byte[] str, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(str);
        return Base64.toBase64String(signature.sign());
    }

    public static boolean verify(byte[] str, PublicKey publicKey, String sign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(str);
        return signature.verify(Base64.decode(sign));
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(KEY_LENGTH);
        return generator.generateKeyPair();
    }

    public static String getPrivateKeyString(PrivateKey privateKey) {
        return Base64.toBase64String(privateKey.getEncoded());
    }

    public static String getPublicKeyString(PublicKey publicKey) {
        return Base64.toBase64String(publicKey.getEncoded());
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance(ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.toBase64String(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance(ALGORITHM);
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), StandardCharsets.UTF_8);
    }
}
