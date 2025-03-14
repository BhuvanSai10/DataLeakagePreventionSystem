/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hybridcrypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class BlowFish {
     // Blowfish encryption and decryption
    public static byte[] blowfishEncrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input);
    }

    public static byte[] blowfishDecrypt(byte[] input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(input);
    }
    
}
