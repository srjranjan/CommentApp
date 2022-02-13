package com.srj.commentapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public interface Encryption {
    String ALGO = "AES"; // Default uses ECB PKCS5Padding

    @RequiresApi(api = Build.VERSION_CODES.O)
    static String encodeKey(String str) {
        byte[] encoded = Base64.getEncoder().encode(str.getBytes());
        return new String(encoded);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static String encrypt(String Data, String secret) throws Exception {
        Key key = generateKey(secret);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal);
        return encryptedValue;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static String decrypt(String strToDecrypt, String secret) {
        try {
            Key key = generateKey(secret);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static Key generateKey(String secret) {
        byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
        Key key = new SecretKeySpec(decoded, ALGO);
        return key;
    }
}
