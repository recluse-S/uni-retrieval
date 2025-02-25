package com.retrieval.Server.impl;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.retrieval.Mapper.IRMapper;
import com.retrieval.Server.WxPayService;
import com.retrieval.Util.WXPayUtil;
import com.retrieval.pojo.ApiResponseBody;
import com.retrieval.pojo.JwtUtils;
import com.retrieval.pojo.Result;
import com.retrieval.pojo.Vo.WxPayOrderReqVO;
import com.retrieval.pojo.WxPayV3Bean;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wechat.pay.java.core.http.Constant.*;

/**
 * @author lsh
 * @ClassName WxPayServiceImpl
 * @description: TODO
 * @date 2023年10月24日
 */
@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private WxPayV3Bean wxPayV3Bean;

    @Autowired
    private IRMapper irMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseBody createOrder(WxPayOrderReqVO req, HttpServletRequest request) throws Exception {
        // 生成商户订单号
        String tradeNo = getTradeNo();
        // 使用自动更新平台证书的RSA配置，配置微信支付的自动证书管理功能
        Config config = new RSAPublicKeyConfig.Builder()
                .merchantId(wxPayV3Bean.getMchId())
                .publicKeyFromPath(wxPayV3Bean.getKeyPath())
                .merchantSerialNumber(wxPayV3Bean.getMchSerialNo())
                .apiV3Key(wxPayV3Bean.getApiKey())
                .privateKeyFromPath(wxPayV3Bean.getPrivateKeyPath())
                .publicKeyId(wxPayV3Bean.getPublicKeyId())
                .build();
        // 构建service,用于处理JSAPI支付相关的操作
        JsapiService service = new JsapiService.Builder().config(config).build();

        // 创建预支付订单的请求对象
        PrepayRequest prepayRequest = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(Integer.valueOf(req.getAmount().toString()));
        prepayRequest.setAmount(amount);
        prepayRequest.setAppid(wxPayV3Bean.getAppId());
        prepayRequest.setMchid(wxPayV3Bean.getMchId());
        prepayRequest.setNotifyUrl(wxPayV3Bean.getNotifyUrl());
        prepayRequest.setDescription(req.getDescription());
        prepayRequest.setOutTradeNo(tradeNo);
        prepayRequest.setAttach(req.getOrderType());

        //根据token拿到openid,指定该预支付订单的支付者身份
        String token = request.getHeader("token");
        String openId = irMapper.getOpenIdByToken(token);

        Payer payer = new Payer();
        payer.setOpenid(openId);
        prepayRequest.setPayer(payer);

        try {
            // 调用下单方法，得到应答
            PrepayResponse response = service.prepay(prepayRequest);
            System.out.println("Prepay response: " + response);

            // 检查prepay接口返回结果
            if (response == null || response.getPrepayId() == null) {
                System.out.println("预支付接口调用失败，返回为空或prepayId为空");
                return ApiResponseBody.error("预支付接口调用失败");
            }

            Map<String, Object> params = new HashMap<>();
            Long timeStamp = System.currentTimeMillis() / 1000;
            params.put("timeStamp", timeStamp);
            String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
            params.put("nonceStr", substring);

            String signatureStr = Stream.of(wxPayV3Bean.getAppId(), String.valueOf(timeStamp), substring, "prepay_id=" + response.getPrepayId())
                    .collect(Collectors.joining("\n", "", "\n"));
            String sign = WXPayUtil.getSign(signatureStr, wxPayV3Bean.getPrivateKeyPath());
            params.put("paySign", sign);
            params.put("package", "prepay_id=" + response.getPrepayId());
            params.put("tradeNo", tradeNo);

            try {
                int userId = -1;
                Claims claims = JwtUtils.parseJWT(token);
                userId = (int) claims.get("id");
                irMapper.increaseVips(userId, tradeNo);
            } catch (Exception e) {
                throw new ApiException(e.toString());
            }

            return ApiResponseBody.success(params);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseBody.error("预支付接口调用异常: " + e.getMessage());
        }
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ApiResponseBody payNotify(HttpServletRequest request) throws Exception {
//        //读取请求体的信息
//        ServletInputStream inputStream = request.getInputStream();
//        StringBuffer stringBuffer = new StringBuffer();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String s;
//        //读取回调请求体
//        while ((s = bufferedReader.readLine()) != null) {
//            stringBuffer.append(s);
//        }
//        String s1 = stringBuffer.toString();
//        String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
//        String nonce = request.getHeader(WECHAT_PAY_NONCE);
//        String signType = request.getHeader("Wechatpay-Signature-Type");
//        String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
//        String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
//        // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
//        // 没有的话，则构造一个
//        NotificationConfig config = new RSAAutoCertificateConfig.Builder()
//                .merchantId(wxPayV3Bean.getMchId())
//                .privateKeyFromPath(wxPayV3Bean.getKeyPath())
//                .merchantSerialNumber(wxPayV3Bean.getMchSerialNo())
//                .apiV3Key(wxPayV3Bean.getApiKey())
//                .build();
//        // 初始化 NotificationParser
//        NotificationParser parser = new NotificationParser(config);
//        RequestParam requestParam = new RequestParam.Builder()
//                .serialNumber(serialNo)
//                .nonce(nonce)
//                .signature(signature)
//                .timestamp(timestamp)
//                // 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
//                .signType(signType)
//                .body(s1)
//                .build();
//        Transaction parse = parser.parse(requestParam, Transaction.class);
//        log.info("-----------------------parse = " + parse);
//
////        //根据token拿到openid,指定该预支付订单的支付者身份
////        String token = request.getHeader("token");
////
////        try {
////            int userId = -1;
////            Claims claims = JwtUtils.parseJWT(token);
////            userId = (int) claims.get("id");
////            irMapper.increaseVips(userId);
////        } catch (Exception e) {
////            throw new ApiException(e.toString());
////        }
//
////        try {
////            String status = orderMapper.queryPayOrderStatus(parse.getOutTradeNo().toString());
////            if (status.equals("1")) {
////                log.info("-----------------------该订单：" + parse.getOutTradeNo() + "已经进行回调，不可重复回调");
////                return ApiResponseBody.defaultSuccess();
////            }
////            updateAccountDetail(parse);
////        } catch (Exception e) {
////            throw new ApiException(e.toString());
////        }
//
//        log.info("-----------------------回调完成-----------------------");
//        return ApiResponseBody.success();
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseBody payNotify(HttpServletRequest request) throws Exception {
        // 读取请求体的信息
        ServletInputStream inputStream = request.getInputStream();
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        // 读取回调请求体
        while ((s = bufferedReader.readLine()) != null) {
            stringBuffer.append(s);
        }
        String s1 = stringBuffer.toString();

        // 获取微信支付回调中的头部信息
        String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
        String nonce = request.getHeader(WECHAT_PAY_NONCE);
        String signType = request.getHeader("Wechatpay-Signature-Type");
        String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
        String signature = request.getHeader(WECHAT_PAY_SIGNATURE);

        // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
        // 没有的话，则构造一个
        NotificationConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayV3Bean.getMchId())
                .privateKeyFromPath(wxPayV3Bean.getKeyPath())
                .merchantSerialNumber(wxPayV3Bean.getMchSerialNo())
                .apiV3Key(wxPayV3Bean.getApiKey())
                .build();

        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(config);
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serialNo)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                // 若未设置 signType，默认值为 WECHATPAY2-SHA256-RSA2048
                .signType(signType)
                .body(s1)
                .build();

        // 解析通知
        Transaction parse = parser.parse(requestParam, Transaction.class);
        log.info("-----------------------parse = " + parse);

        // 获取商户订单号（out_trade_no）和微信支付订单号（transaction_id）
        String outTradeNo = parse.getOutTradeNo();  // 商户订单号
        String transactionId = parse.getTransactionId();  // 微信支付订单号

        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");

        log.info("商户订单号: " + outTradeNo);
        log.info("微信支付订单号: " + transactionId);

//        // 根据交易结果处理业务逻辑
//        if ("SUCCESS".equals(parse.getTradeState())) {
//            // 如果支付成功，您可以根据商户订单号更新订单状态
//            try {
//                // 查询订单状态，避免重复回调
//                String status = orderMapper.queryPayOrderStatus(outTradeNo);
//                if ("1".equals(status)) {
//                    log.info("该订单：" + outTradeNo + "已经进行回调，不可重复回调");
//                    return ApiResponseBody.defaultSuccess();
//                }
//                // 更新订单状态为支付成功
//                updateAccountDetail(parse);
//            } catch (Exception e) {
//                throw new ApiException(e.toString());
//            }
//        } else {
//            // 支付失败的处理逻辑
//            log.error("支付失败，订单号：" + outTradeNo);
//        }

        log.info("-----------------------回调完成-----------------------");
        return ApiResponseBody.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseBody queryPayOrder(String tradeNo) {
//        String status = orderMapper.queryPayOrderStatus(tradeNo);
//        if (status.equals("1")) {
//            return ApiResponseBody.defaultSuccess("SUCCESS");
//        }
//        // 使用自动更新平台证书的RSA配置，配置微信支付的自动证书管理功能
//        Config config =
//                new RSAAutoCertificateConfig.Builder()
//                        .merchantId(wxPayV3Bean.getMchId())
//                        .privateKeyFromPath(wxPayV3Bean.getKeyPath())
//                        .merchantSerialNumber(wxPayV3Bean.getMchSerialNo())
//                        .apiV3Key(wxPayV3Bean.getApiKey())
//                        .build();
//        // 构建service,用于处理JSAPI支付相关的操作
//        JsapiService service = new JsapiService.Builder().config(config).build();
//
//        //根据商户订单号查询支付订单
//        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
//        queryRequest.setMchid(wxPayV3Bean.getMchId());
//        queryRequest.setOutTradeNo(tradeNo);
//        Transaction transaction = service.queryOrderByOutTradeNo(queryRequest);
//        log.info("-----------------------支付状态：" + transaction.getTradeState());
//        updateAccountDetail(transaction);
//        return ApiResponseBody.defaultSuccess(transaction.getTradeState().toString());
        return ApiResponseBody.success();
    }

    @Override
    public Integer getAmount() {
        Integer amount = irMapper.getAmount();
        return amount;
    }

    /**
     * 时间+id为订单号
     * @param
     * @return
     */
    public String getTradeNo() {
        try {
            // 获取当前时间戳，截取最后8位
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(6); // 取最后8位
            // 获取唯一ID (UUID)，并截取前16位
            String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 16); // 截取16位
            // 生成订单号: 时间戳后8位 + 唯一ID前16位
            String tradeNo = timestamp + uniqueId;
            return tradeNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public String getTradeNo() {
//        String idStr = IdTool.getIdStr();
//        long timestamp = DateUtil.getDate();
//        //序列号是为了保证同一毫秒内生成的订单号的唯一性
//        AtomicInteger sequence = new AtomicInteger(0);
//        int nextSequence = sequence.getAndIncrement();
//
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] messageDigest = md.digest(String.valueOf(nextSequence).getBytes());
//
//            BigInteger no = new BigInteger(1, messageDigest);
//            String encrypted = no.toString(10); // 将十六进制转为十进制表示的字符串
//
//            // 如果加密结果长度超过20位，则截取前20位
//            if (encrypted.length() > 20) {
//                encrypted = encrypted.substring(0, 20);
//            }
//            String tradeNo = timestamp + idStr;
//            return tradeNo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public void updateAccountDetail(Transaction parse) {
//        //更新订单状态
//        Map<String, Object> map = new HashMap<>();
//        map.put("tradeNo", parse.getOutTradeNo().toString());
//        map.put("transactionId", parse.getTransactionId().toString());
//        orderMapper.updateOrderStatus(map);
//        //保存充值流水账单
//        String outTradeNo = parse.getOutTradeNo();
//        Map<String, Object> accountMap = accountUserMapper.getAccountUser(outTradeNo);
//        Map<String, Object> billMap = new HashMap<>();
//        billMap.put("id", IdTool.getIdStr());
//        billMap.put("amount", parse.getAmount().getTotal());
//        billMap.put("accountUserId", accountMap.get("accountUserId"));
//        billMap.put("payOrderId", accountMap.get("payOrderId"));
//        billMap.put("agoBalance", accountMap.get("balance"));
//        billMap.put("createDate", DateUtil.getTime());
//        billMap.put("laterBalance", BigDecimal.valueOf(parse.getAmount().getTotal()).add(BigDecimal.valueOf(Integer.valueOf(accountMap.get("balance").toString()))));
//        billMap.put("billType", "pay");
//        billMap.put("tradeType", "wechat-applet");
//        log.info("-----------------------本次支付流水账单信息：" + billMap);
//        orderMapper.insertBillLog(billMap);
//
//        //更新户号的余额
//        Map<String, Object> accountUserMap = new HashMap<>();
//        accountUserMap.put("accountUserId", accountMap.get("accountUserId"));
//        accountUserMap.put("balance", Long.valueOf(parse.getAmount().getTotal()) + Long.valueOf(accountMap.get("balance").toString()));
//        accountUserMapper.updateBalance(accountUserMap);
//    }

}
