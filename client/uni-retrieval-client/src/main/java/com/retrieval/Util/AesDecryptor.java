package com.retrieval.Util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

public class AesDecryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String decrypt(String encryptedData, String sessionKey, String iv) throws Exception {
        if (encryptedData == null) {
            throw new IllegalArgumentException("The encryptedData cannot be null");
        }
        if (sessionKey == null) {
            throw new IllegalArgumentException("The sessionKey cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("The iv cannot be null");
        }

        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);

        return new String(decryptedBytes, "UTF-8");
    }
}
