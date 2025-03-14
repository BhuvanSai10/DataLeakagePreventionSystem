/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hybridcrypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class TripleDES {
    // Triple DES encryption and decryption
    public static byte[] tripleDesEncrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    public static byte[] tripleDesDecrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }
}
