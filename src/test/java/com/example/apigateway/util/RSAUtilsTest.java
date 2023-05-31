package com.example.apigateway.util;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Date 2022-07-02 17:43
 */
class RSAUtilsTest {
    private static final String priKey1024 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI9m7FjSfQ9jxSTNT6W9OUMBCXGS/qCh1iRYNVJt/EP3RAqR79ybNDXzqK98HDpZAQyzxPRQWkvsGw+aotmEiBq220miRab4d8uvk9j2QV+sfl6WN9gJr8BpPf0cb+D93cjxfnnTdw8w8Gith/LCZGjIYaQ5W+sZsoQJHE7EzZmfAgMBAAECgYA81yD9SkiO5/hzdbwOCGsr91unWQM46ZCuGNJ+p3U47MZsaaBHi32qSjdORKzBQJPGbNrqANFelsygYS4odSc887NpMdpg97AfwgIpqETmsnvPW1/sXnBdSMeJOaM0x6v8HRqyANYYRZe9bky6cMleo/JIsr2ZWWZ5UzMkIryueQJBAME4uBiXo31K8EQZLBbSy2BcwSiYZOxjUylCN2zSogGfHtmlPrr3FTd/887X352AAbm1HSDEVoTplmgWGy75tcsCQQC9/nImF3/k+MTl+pAywlg8mop1y8r2zn8Lst8PDxrZXJkpJBnaht8aIhYl67OZO7oQhBpoUAXfgEmn3rNS8ND9AkBeJcHmEbUSrroFpHJMKKf6KxffLOo4GGmD+ATDnJpdyeehSxgWx5/p7AmjJqKJvr6YezxXI/O4TY0A6IyURg4jAkA8W50/5K9Iuo8y0IsG1R7bAe2Cxp9QEQHjYYvjaNUKwJQXsdRUzAwMbU/D9EE2KPdPT+hjwyhafJLptaIac06xAkEAhEGyCRFNl58Skgxvqh4vI9Kkme1WiL6/vrezkAIgHfN168J2OiyHdRQty58OiB/h98p5iPvj1+uZBVJKmnA+yQ==";
    private static final String pubKey1024 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPZuxY0n0PY8UkzU+lvTlDAQlxkv6godYkWDVSbfxD90QKke/cmzQ186ivfBw6WQEMs8T0UFpL7BsPmqLZhIgatttJokWm+HfLr5PY9kFfrH5eljfYCa/AaT39HG/g/d3I8X5503cPMPBorYfywmRoyGGkOVvrGbKECRxOxM2ZnwIDAQAB";

    private static final String priKey512 = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA50CiEejI6qLfCZIhe8I4i/sZVX1t3gMtoPua+Hh+VkKtDnVaAtrT9fBnHpMS4hGZx5OZTbdjXAxPE1S2Bwyl+wIDAQABAkAHCE8lbkfV4tgXiWsdQV3I2/z8PZE+xCf6Eky3K6rj4ppjTx8zk5Zz2Ho0MUGx2asa0HIhF8QXfRG0gtZKewzJAiEA/Q1WBFR9hZ/HNNekyvIO25FN7BskB63lzPdf9xhHYm0CIQDp8kkD5cDZxvLbGivSpjvoqrSJKZj8d2kFLoL3BjupBwIhAIKEQFtXYK5s7IgauUpbcsJYvYILtZeoX6BJ7ts5UIg5AiEAnae2RtcdGGW3IapIYAPAmbH4Sfrray/5aRGIFnpv2B8CIECyBqrzXF7gohPruatKFgP9cuzDgq2FrtxXGM4IyeOR";
    private static final String pubKey512 = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOdAohHoyOqi3wmSIXvCOIv7GVV9bd4DLaD7mvh4flZCrQ51WgLa0/XwZx6TEuIRmceTmU23Y1wMTxNUtgcMpfsCAwEAAQ==";

    private static final String str = "abatchcode-random-number001";
    private static final String sign = "V4/IsBuwxf2xk4LcMPZTMXk0Jp9yF32Q5K2oSZxCnaqbQoOezUMiuNoZW+8sTG+X0dylgRKskpAxVCAZum5Rpg==";

    @Test
    void should_print_keypair_when_generate_by_utils() throws NoSuchAlgorithmException {
        final KeyPair keyPair = RSAUtils.generateKeyPair();
        final String publicKey = Base64.toBase64String(keyPair.getPublic().getEncoded());
        final String privateKey = Base64.toBase64String(keyPair.getPrivate().getEncoded());
        System.out.println(privateKey);
        System.out.println(publicKey);
    }

    @Test
    void should_success_when_sign_and_verify_by_1024_key_length() throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final PrivateKey privateKey = RSAUtils.getPrivateKey(priKey1024);
        final PublicKey publicKey = RSAUtils.getPublicKey(pubKey1024);

        final String sign = RSAUtils.sign(str.getBytes(), privateKey);
        System.out.println("[sign]: " + sign);

        final long start = System.currentTimeMillis();
        final boolean verify = RSAUtils.verify(str.getBytes(), publicKey, sign);
        final long end = System.currentTimeMillis();
        System.out.println("[time]: " + (end - start));

        Assertions.assertTrue(verify);
    }

    @Test
    void should_success_when_sign_and_verify_by_512_key_length() throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final PrivateKey privateKey = RSAUtils.getPrivateKey(priKey512);
        final PublicKey publicKey = RSAUtils.getPublicKey(pubKey512);

        final String sign = RSAUtils.sign(str.getBytes(), privateKey);
        System.out.println("[sign]: " + sign);

        final long start = System.currentTimeMillis();
        final boolean verify = RSAUtils.verify(str.getBytes(), publicKey, sign);
        final long end = System.currentTimeMillis();
        System.out.println("[time]: " + (end - start));

        Assertions.assertTrue(verify);
    }

    @Test
    void should_success_when_encrypt_and_decrypt_by_512_key_length() throws Exception {
        final PrivateKey privateKey = RSAUtils.getPrivateKey(priKey512);
        final PublicKey publicKey = RSAUtils.getPublicKey(pubKey512);
        final String encrypt = RSAUtils.encrypt(str, publicKey);
        System.out.println("[en]: " + encrypt);

        final long start = System.currentTimeMillis();
        final String decrypt = RSAUtils.decrypt(encrypt, privateKey);
        final long end = System.currentTimeMillis();
        System.out.println("[de]: " + decrypt);
        System.out.println("[time]: " + (end - start));

        Assertions.assertEquals(str, decrypt);
    }
}
