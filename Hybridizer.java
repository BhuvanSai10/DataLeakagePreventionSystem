/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hybridcrypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Hybridizer {
    // Generate a random symmetric key for AES, Triple DES, and Blowfish
    private static SecretKey generateSymmetricKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    // Generate RSA key pair for public and private key
    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
    // AES encryption and decryption
    private static byte[] aesEncrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    private static byte[] aesDecrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    // Triple DES encryption and decryption
    private static byte[] tripleDesEncrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    private static byte[] tripleDesDecrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    // Blowfish encryption and decryption
    private static byte[] blowfishEncrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    private static byte[] blowfishDecrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    // RSA encryption and decryption
    private static byte[] rsaEncrypt(byte[] input, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(input);
    }

    private static byte[] rsaDecrypt(byte[] input, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(input);
    }
    // Hybrid encryption
    public static String hybridEncrypt(String plaintext) throws Exception {
        // Generate symmetric keys for AES, Triple DES, and Blowfish
        SecretKey aesKey = generateSymmetricKey("AES", 128);
        SecretKey tripleDesKey = generateSymmetricKey("DESede", 168);
        SecretKey blowfishKey = generateSymmetricKey("Blowfish", 128);

        // Generate RSA key pair
        KeyPair rsaKeyPair = generateRSAKeyPair();

        // Encrypt the plaintext using the symmetric keys
        byte[] encryptedAes = aesEncrypt(plaintext.getBytes(), aesKey);
        byte[] encryptedTripleDes = tripleDesEncrypt(plaintext.getBytes(), tripleDesKey);
        byte[] encryptedBlowfish = blowfishEncrypt(plaintext.getBytes(), blowfishKey);

        // Encrypt the symmetric keys using RSA public key
        byte[] encryptedAesKey = rsaEncrypt(aesKey.getEncoded(), rsaKeyPair.getPublic());
        byte[] encryptedTripleDesKey = rsaEncrypt(tripleDesKey.getEncoded(), rsaKeyPair.getPublic());
        byte[] encryptedBlowfishKey = rsaEncrypt(blowfishKey.getEncoded(), rsaKeyPair.getPublic());

        // Combine all encrypted components into a single string
        String encryptedText = Base64.getEncoder().encodeToString(encryptedAes) + ","
                + Base64.getEncoder().encodeToString(encryptedTripleDes) + ","
                + Base64.getEncoder().encodeToString(encryptedBlowfish) + ","
                + Base64.getEncoder().encodeToString(encryptedAesKey) + ","
                + Base64.getEncoder().encodeToString(encryptedTripleDesKey) + ","
                + Base64.getEncoder().encodeToString(encryptedBlowfishKey);

        // Combine the keys into a single string
        String keyString = Base64.getEncoder().encodeToString(rsaKeyPair.getPrivate().getEncoded()) + ","
                + Base64.getEncoder().encodeToString(aesKey.getEncoded()) + ","
                + Base64.getEncoder().encodeToString(tripleDesKey.getEncoded()) + ","
                + Base64.getEncoder().encodeToString(blowfishKey.getEncoded());

        return keyString + "#" + encryptedText;
    }

    // Hybrid decryption
    public  String hybridDecrypt(String encryptedTextWithKeys) throws Exception {
        // Split the encrypted string into components
        String[] parts = encryptedTextWithKeys.split("#");
        String keysString = parts[0];
        String encryptedText = parts[1];

        // Split the keys string
        String[] keyComponents = keysString.split(",");
        byte[] rsaPrivateKeyBytes = Base64.getDecoder().decode(keyComponents[0]);
        byte[] aesKeyBytes = Base64.getDecoder().decode(keyComponents[1]);
        byte[] tripleDesKeyBytes = Base64.getDecoder().decode(keyComponents[2]);
        byte[] blowfishKeyBytes = Base64.getDecoder().decode(keyComponents[3]);

        // Reconstruct the keys from bytes
        PrivateKey rsaPrivateKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(rsaPrivateKeyBytes));
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
        SecretKey tripleDesKey = new SecretKeySpec(tripleDesKeyBytes, "DESede");
        SecretKey blowfishKey = new SecretKeySpec(blowfishKeyBytes, "Blowfish");

        // Decrypt the ciphertext using the symmetric keys
        String decryptedAes = new String(aesDecrypt(Base64.getDecoder().decode(encryptedText.split(",")[0]), aesKey));
        String decryptedTripleDes = new String(tripleDesDecrypt(Base64.getDecoder().decode(encryptedText.split(",")[1]), tripleDesKey));
        String decryptedBlowfish = new String(blowfishDecrypt(Base64.getDecoder().decode(encryptedText.split(",")[2]), blowfishKey));

        // Verify that all decrypted versions match
        if (!decryptedAes.equals(decryptedTripleDes) || !decryptedAes.equals(decryptedBlowfish)) {
            throw new Exception("Decryption failed: Inconsistent results");
        }

        return decryptedAes;
    }
}
