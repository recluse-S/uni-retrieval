//package com.retrieval.controller;
//
//import com.retrieval.Server.impl.DecryptionService;
//import com.retrieval.Server.impl.WeChatService;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
//public class WeChatController {
//
//    @Autowired
//    private WeChatService weChatService;
//
//    @Autowired
//    private DecryptionService decryptionService;
//
//    @PostMapping("/decryptPhoneNumber")
//    public Map<String, Object> decryptPhoneNumber(@RequestBody Map<String, String> request) {
//        System.out.println("-------------------------------------");
//        try {
//            String code = request.get("code");
//            String encryptedData = request.get("encryptedData");
//            String iv = request.get("iv");
//
//            Map<String, Object> sessionData = weChatService.getSessionKeyAndOpenId(code);
//            if (sessionData == null || !sessionData.containsKey("session_key")) {
//                return createErrorResponse("获取 session_key 失败");
//            }
//
//            String sessionKey = (String) sessionData.get("session_key");
//            Map<String, Object> decryptedData = decryptionService.decryptPhoneNumber(encryptedData, iv, sessionKey);
//
//            return createSuccessResponse("解密成功", decryptedData);
//        } catch (Exception e) {
//            return createErrorResponse("解密失败: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/wechat/getOpenid")
//    public ResponseEntity<?> getOpenid(@RequestBody String code) {
//        try {
//            JSONObject openidData = weChatService.getOpenid(code);
//            return ResponseEntity.ok(openidData.toString());  // 返回获取的 openid 数据
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
//
//    private Map<String, Object> createSuccessResponse(String message, Map<String, Object> data) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 200);
//        response.put("message", message);
//        response.put("data", data);
//        return response;
//    }
//
//    private Map<String, Object> createErrorResponse(String message) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 500);
//        response.put("message", message);
//        return response;
//    }
//}
//
//
