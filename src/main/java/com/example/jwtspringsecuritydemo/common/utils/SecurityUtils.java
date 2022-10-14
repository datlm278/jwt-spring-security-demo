package com.example.jwtspringsecuritydemo.common.utils;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityUtils {
    public static byte[] signature(String data, PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, IOException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(data.getBytes(StandardCharsets.UTF_8));
        return sign.sign();
    }

    public static PrivateKey getPrivateKey(String resourceName) throws IOException {
        InputStream stream = SecurityUtils.class.getClassLoader().getResourceAsStream(resourceName);
        return getPrivateKey(new InputStreamReader(stream));
    }

    public static PrivateKey getPrivateKey(File privateKeyFile) throws IOException {
        PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile));
        Object keyObject = pemParser.readObject();
        PEMDecryptorProvider provider = new JcePEMDecryptorProviderBuilder().build(null);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

        KeyPair keyPair;

        if (keyObject instanceof PEMEncryptedKeyPair) {
            keyPair = converter.getKeyPair(((PEMEncryptedKeyPair) keyObject).decryptKeyPair(provider));
        } else {
            keyPair = converter.getKeyPair((PEMKeyPair) keyObject);
        }
        return keyPair.getPrivate();
    }

    public static PrivateKey getPrivateKey(Reader reader) throws IOException {
        PEMParser pemParser = new PEMParser(reader);
        Object keyObject = pemParser.readObject();
        PEMDecryptorProvider provider = new JcePEMDecryptorProviderBuilder().build(null);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

        KeyPair keyPair;

        if (keyObject instanceof PEMEncryptedKeyPair) {
            keyPair = converter.getKeyPair(((PEMEncryptedKeyPair) keyObject).decryptKeyPair(provider));
        } else {
            keyPair = converter.getKeyPair((PEMKeyPair) keyObject);
        }
        return keyPair.getPrivate();
    }

    private PrivateKey getPrivateKey() throws InvalidKeySpecException,
            NoSuchAlgorithmException, URISyntaxException, IOException {
        byte[] keyBytes = Files.readAllBytes(
                Paths.get(ClassLoader.getSystemResource("publickey.pem").toURI()));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String resourceName) throws IOException {
        InputStream inputStream = SecurityUtils.class.getClassLoader().getResourceAsStream(resourceName);
        return getPublicKey(new InputStreamReader(inputStream));
    }

    public static PublicKey getPublicKey(File publicKeyFile) throws IOException {
        PEMParser pemParser = new PEMParser(new FileReader(publicKeyFile));
        Object keyObject = pemParser.readObject();

        if (keyObject instanceof SubjectPublicKeyInfo) {
            return (new JcaPEMKeyConverter()).getPublicKey((SubjectPublicKeyInfo) keyObject);
        }
        return null;
    }

    public static PublicKey getPublicKey(InputStreamReader reader) throws IOException {
        PEMParser pemParser = new PEMParser(reader);
        Object keyObject = pemParser.readObject();

        if (keyObject instanceof SubjectPublicKeyInfo) {
            return (new JcaPEMKeyConverter()).getPublicKey((SubjectPublicKeyInfo) keyObject);
        }
        return null;
    }

    private static PublicKey getPublicKey() throws URISyntaxException,
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(
                Paths.get(ClassLoader.getSystemResource("publickey.pem").toURI()));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static boolean verifySignature(String message, String sign, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        System.out.println(sign);
        byte[] signatureBytes = Base64.getUrlDecoder().decode(sign);
        return signature.verify(signatureBytes);
    }
}
