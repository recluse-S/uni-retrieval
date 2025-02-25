package com.retrieval.Util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.json.JSONObject;

public class WeChatDecryptUtils {

    // 解密函数
    public static String decrypt(String sessionKey, String encryptedData, String iv) throws Exception {
        // 先解码 sessionKey 和 iv
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        // 使用 AES 解密
        SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new javax.crypto.spec.IvParameterSpec(ivBytes));
        byte[] decryptedData = cipher.doFinal(encryptedDataBytes);

        // 转换为字符串
        String result = new String(decryptedData, "UTF-8");
        JSONObject jsonObject = new JSONObject(result);

        // 返回手机号等数据
        return jsonObject.getString("phoneNumber");
    }
}
