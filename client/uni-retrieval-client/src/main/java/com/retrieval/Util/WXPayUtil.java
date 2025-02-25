package com.retrieval.Util;

import com.wechat.pay.java.core.util.PemUtil;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Random;

/**
 * @project
 * @Classname WXPayUtil
 * @Description TODO
 * @Author: lsh
 * @CreateTime: 2023-10-24  16:00
 */
public class WXPayUtil {

    public static String getSign(String signatureStr,String privateKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException, URISyntaxException {
        //replace 根据实际情况，不一定都需要
        String replace = privateKey.replace("\\n", "\n");
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKeyFromPath(replace);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(merchantPrivateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(sign.sign());
    }

}
