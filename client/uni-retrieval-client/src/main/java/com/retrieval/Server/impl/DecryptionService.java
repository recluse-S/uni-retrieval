package com.retrieval.Server.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class DecryptionService {

    public Map<String, Object> decryptPhoneNumber(String encryptedData, String iv, String sessionKey) throws Exception {
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] decryptedData = cipher.doFinal(encryptedDataBytes);

        String decryptedDataStr = new String(decryptedData, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(decryptedDataStr, Map.class);
    }
}
