//package com.retrieval.bo;
//
//import com.retrieval.Util.WXPayUtil;
//import com.retrieval.config.PayInfoConfig;
//import com.wechat.pay.java.core.Config;
//import com.wechat.pay.java.core.RSAAutoCertificateConfig;
//import com.wechat.pay.java.core.exception.ServiceException;
//import com.wechat.pay.java.core.exception.ValidationException;
//import com.wechat.pay.java.core.notification.NotificationConfig;
//import com.wechat.pay.java.core.notification.NotificationParser;
//import com.wechat.pay.java.core.notification.RequestParam;
////import com.wechat.pay.java.core.util.StringUtil;
//import com.wechat.pay.java.service.payments.jsapi.JsapiService;
//import com.wechat.pay.java.service.payments.jsapi.model.*;
//import com.wechat.pay.java.service.payments.model.Transaction;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import static com.wechat.pay.java.core.http.Constant.*;
//
///**
// *
// * @author Rick chou
// * @date 2024/7/16 9:10
// * 微信支付BO
// *
// */
//@Slf4j
//@Component
//public class WechatPayBo {
//
//    @Autowired
//    private static PayInfoConfig payInfoConfig;
//
//    public static PayInfoConfig getConfig() {
//        return payInfoConfig;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 9:35
//     * 构建支付请求SERVICE
//     *
//     */
//    public static JsapiService getService(){
//        PayInfoConfig payInfoConfig = getConfig();
//        Config config = new RSAAutoCertificateConfig.Builder()
//                .merchantId(payInfoConfig.getMchId())
//                .privateKeyFromPath(payInfoConfig.getKeyPath())
//                .merchantSerialNumber(payInfoConfig.getMchSerialNo())
//                .apiV3Key(payInfoConfig.getApiKey())
//                .build();
//        JsapiService service = new JsapiService.Builder().config(config).build();
//        return service;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 10:31
//     * 构造NOTIFY_CONFIG
//     *
//     */
//    private static NotificationConfig buildNotifyConfig(){
//        PayInfoConfig payInfoConfig = getConfig();
//        NotificationConfig config = new RSAAutoCertificateConfig.Builder()
//                .merchantId(payInfoConfig.getMchId())
//                .privateKeyFromPath(payInfoConfig.getKeyPath())
//                .merchantSerialNumber(payInfoConfig.getMchSerialNo())
//                .apiV3Key(payInfoConfig.getApiKey())
//                .build();
//        return config;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 9:35
//     * 构建支付请求参数
//     *
//     */
//    private static PrepayRequest buildParam(Integer price, String orderId, String userId){
//        PayInfoConfig payInfoConfig = getConfig();
//        PrepayRequest prepayRequest = new PrepayRequest();
//        Amount amount = new Amount();
//        amount.setTotal(price);
//        prepayRequest.setAmount(amount);
//        prepayRequest.setAppid(payInfoConfig.getAppId());
//        prepayRequest.setMchid(payInfoConfig.getMchId());
//        prepayRequest.setNotifyUrl(payInfoConfig.getNotifyUrl());				// 回调接口地址
//        prepayRequest.setDescription("微信支付");
//        prepayRequest.setOutTradeNo(orderId);       // 订单号
//        prepayRequest.setAttach("member");                                     // 订单类型(回调时可根据这个数据辨别订单类型或其他)
//
//        //根据token拿到openid,指定该预支付订单的支付者身份
//        Payer payer = new Payer();
//        payer.setOpenid(WeixinUserBo.getOpenId(userId));
//        prepayRequest.setPayer(payer);
//        return prepayRequest;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 9:53
//     * 解析支付结果
//     *
//     */
//    private static Map<String,Object> parsePay(PrepayResponse response){
//        PayInfoConfig payInfoConfig = getConfig();
//        Map<String, Object> params = new HashMap<>();
//        Long timeStamp = System.currentTimeMillis() / 1000;
//        String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
//        String signatureStr = Stream.of(
//                payInfoConfig.getAppId(),
//                String.valueOf(timeStamp),
//                substring,
//                "prepay_id=" + response.getPrepayId()
//        ).collect(Collectors.joining("\n", "", "\n"));
//        String sign = WXPayUtil.getSign(signatureStr, payInfoConfig.getKeyPath());
//        params.put("timeStamp", String.valueOf(timeStamp));
//        params.put("nonceStr", substring);
//        params.put("paySign", sign);
//        params.put("signType", "RSA");
//        params.put("package", "prepay_id=" + response.getPrepayId());
//        return params;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 10:33
//     * 解析回调结果
//     *
//     */
//    private static RequestParam parseNotify(HttpServletRequest request)throws IOException {
//        String data = StringUtil.getStringForInput(request.getInputStream());
//        String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
//        String nonce = request.getHeader(WECHAT_PAY_NONCE);
//        String signType = request.getHeader("Wechatpay-Signature-Type");
//        String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
//        String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
//
//        RequestParam requestParam = new RequestParam.Builder()
//                .serialNumber(serialNo)
//                .nonce(nonce)
//                .signature(signature)
//                .timestamp(timestamp)
//                .signType(signType)         // 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
//                .body(data)
//                .build();
//        return requestParam;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 9:47
//     * 调起支付
//     *
//     */
//    public static Map<String, Object> payment(Integer price, String orderId, String userId){
//        JsapiService service = getService();
//        PrepayRequest prepayRequest = buildParam(price, orderId, userId);
//        PrepayResponse response = service.prepay(prepayRequest);
//        Map<String, Object> result = parsePay(response);
//        result.put("orderId",orderId);
//        return result;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 10:16
//     * 支付回调
//     *
//     */
//    public static Transaction payNotify(HttpServletRequest request) throws Exception {
//        NotificationConfig config = buildNotifyConfig();
//        NotificationParser parser = new NotificationParser(config);
//        RequestParam requestParam = parseNotify(request);
//        Transaction parse = null;
//        try {
//            parse = parser.parse(requestParam, Transaction.class);
//        }catch (ValidationException e){
//            log.error("sign verification failed", e);
//        }
//        return parse;
//    }
//
//    /**
//     *
//     * @author Rick chou
//     * @date 2024/7/16 11:17
//     * 查询订单
//     *
//     */
//    public static Transaction queryPayOrder(String orderId) {
//        PayInfoConfig payInfoConfig = getConfig();
//        JsapiService service = getService();
//        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
//        queryRequest.setMchid(payInfoConfig.getMchId());
//        queryRequest.setOutTradeNo(orderId);
//        Transaction parse = null;
//        try {
//            parse = service.queryOrderByOutTradeNo(queryRequest);
//        }catch (ServiceException e){
//            log.info("code=[%s], message=[%s]\n", e.getErrorCode(), e.getErrorMessage());
//            log.info("reponse body=[%s]\n", e.getResponseBody());
//        }
//        return parse;
//    }
//}
//
