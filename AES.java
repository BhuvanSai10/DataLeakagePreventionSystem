/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hybridcrypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 *
 * @author C.Bhuvansai chowdary
 */
public class AES {
    // AES encryption and decryption
    public static byte[] aesEncrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    public static byte[] aesDecrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }
}
