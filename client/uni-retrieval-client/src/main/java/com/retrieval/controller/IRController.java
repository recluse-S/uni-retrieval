package com.retrieval.controller;

import com.retrieval.Server.IRServer;
import com.retrieval.pojo.*;
import com.retrieval.pojo.Vo.DetailVo;
import com.retrieval.pojo.Vo.LikeVo;
import com.retrieval.pojo.Vo.UniVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/client/retrieval")
public class IRController {

    @Autowired
    private IRServer irServer;

    @GetMapping("/firstSub")
    public Result getFirstSub(){
        try {
            List<MajorPro> majors = irServer.getFirstSub();
            return Result.success(majors);
        }catch (Exception e){
            return Result.error("服务器异常"+e.getMessage());
        }
    }

    @GetMapping("/secSub")
    public Result getSecondSub(@PathParam("id") Integer id , @PathParam("academic") Integer academic) {
        try {
            List<MajorSecondary> majors = irServer.getSecondSub(id ,academic);
            return Result.success(majors);
        }catch (Exception e){
            return Result.error("服务器异常"+e.getMessage());
        }
    }

    @GetMapping("/thirdSub")
    public Result getThirdlySub(@RequestParam Integer father){
        try {
            List<MajorPrimary> majors = irServer.getThirdlySub(father);
            return Result.success(majors);
        }catch (Exception e){
            return Result.error("服务器异常"+e.getMessage());
        }
    }

//    @GetMapping("/getDetail")
//    public Result getDetail(@RequestParam Integer id, @RequestHeader("Authorization") String token) {
//        if (token == null || !token.startsWith("Bearer ")) {
//            return Result.error("token格式错误");
//        }
//        String tokenV = token.substring("Bearer ".length());
//        int userId = -1;
//        try {
//            Claims claims = JwtUtils.parseJWT(tokenV);
//            userId = (int) claims.get("id");
//        } catch (Exception e) {
//            return Result.error("token解析出错: " + e.getMessage());
//        }
//        if (userId == -1) {
//            return Result.error("未获取到token内信息");
//        }
//
//        try {
//            DetailVo result = irServer.getDetail(userId ,id);
//            return Result.success(result);
//        }catch (Exception e){
//            return Result.error("服务器异常"+e.getMessage());
//        }
//    }

    @GetMapping("/getDetail")
    public Result getDetail(@RequestParam Integer id, @RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }

        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        } catch (Exception e) {
            return Result.error("token解析出错: " + e.getMessage());
        }

        if (userId == -1) {
            return Result.error("未获取到token内信息");
        }

        try {
            DetailVo result = irServer.getDetail(userId, id);

            // 设置 liked 属性
            boolean like = irServer.isLike(userId, id);
            result.setLiked(like); // 设置用户是否点赞

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("服务器异常" + e.getMessage());
        }
    }

    @GetMapping("/recommend")
    public Result recommend(@RequestParam Integer day, @RequestHeader(value = "Authorization", required = false) String token) {
        int userId = -1;

        if (token != null && token.startsWith("Bearer ")) {
            String tokenV = token.substring("Bearer ".length());
            try {
                Claims claims = JwtUtils.parseJWT(tokenV);
                userId = (int) claims.get("id");
            } catch (Exception e) {
//                return Result.error("token解析出错: " + e.getMessage());
            }
        }

        List<UniVo> result = irServer.recommend(day);

        for (UniVo retrieval : result) {
            if (userId != -1) {
                boolean like = irServer.isLike(userId, retrieval.getId());
                retrieval.setLiked(like);
            } else {
                retrieval.setLiked(false); // 未登录时默认未点赞
            }
        }

        return Result.success(result);
    }

    @GetMapping("/getHot")
    public Result getHot() {
        Integer hotId = irServer.getHotId();
        List<UniVo> result = irServer.getHot(hotId);
        return Result.success(result);
    }

    @GetMapping("/getHots")
    public Result getHots() {
//        Integer hotId = irServer.getHotId();
        List<UniVo> result = irServer.getHots();
        return Result.success(result);
    }

    @PostMapping("/resultList")
    public Result getReplyList(@RequestBody SelectParam selectParam, @RequestHeader("Authorization") String token) {
//        System.out.println(selectParam);
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }

        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        } catch (Exception e) {
            return Result.error("token解析出错: " + e.getMessage());
        }

        if (userId == -1) {
            return Result.error("未获取到token内信息");
        }

        // 获取分页参数
//        int pageNum = selectParam.getPageNum() != null ? selectParam.getPageNum() : 1;
        int pageSize = selectParam.getPageSize() != null ? selectParam.getPageSize() : 10; // 默认每页10条记录
//        int offset = (pageNum - 1) * pageSize;

//        List<UniVo> result = new ArrayList<>();
//        List<String> major = selectParam.getMajor();
//
//        if (major.isEmpty()) {
//            List<UniVo> r = irServer.getResult(null, selectParam.getInput(), selectParam.getReq(),
//                    selectParam.getLocation(), selectParam.getPostName(),
//                    selectParam.getIsUnder(), selectParam.getPostGrade(),
//                    selectParam.getRemark(), selectParam.getReleaseTime(),
//                    pageSize); // 添加分页参数
//            for (UniVo retrieval : r) {
//                boolean like = irServer.isLike(userId, retrieval.getId());
//                retrieval.setLiked(like); // 设置每个结果的点赞状态
//            }
//            return Result.success(r);
//        }
//
//        for (String s : major) {
//            List<UniVo> r = irServer.getResult(s, selectParam.getInput(), selectParam.getReq(),
//                    selectParam.getLocation(), selectParam.getPostName(),
//                    selectParam.getIsUnder(), selectParam.getPostGrade(),
//                    selectParam.getRemark(), selectParam.getReleaseTime(),
//                    pageSize); // 添加分页参数
//            result.addAll(r);
//        }
//
//        // 遍历结果列表，设置每个结果的 isLiked 属性
//        for (UniVo retrieval : result) {
//            boolean like = irServer.isLike(userId, retrieval.getId());
//            retrieval.setLiked(like); // 设置每个结果的点赞状态
//        }

        List<UniVo> result = new ArrayList<>();
//        List<String> major = selectParam.getMajor();

//        if (major.isEmpty()) {
//            List<UniVo> r = irServer.getResult(Collections.emptyList(), selectParam.getInput(), selectParam.getReq(),
//                    selectParam.getLocation(), selectParam.getPostName(),
//                    selectParam.getIsUnder(), selectParam.getPostGrade(),
//                    selectParam.getRemark(), selectParam.getReleaseTime(),
//                    pageSize);
//            return Result.success(r);
//        }

        List<UniVo> r = irServer.getResult(selectParam.getMajor(), selectParam.getInput(), selectParam.getReq(),
                selectParam.getLocation(), selectParam.getPostName(),
                selectParam.getIsUnder(), selectParam.getPostGrade(),
                selectParam.getRemark(), selectParam.getReleaseTime(),
                pageSize); // 添加分页参数

        result.addAll(r);

        // 遍历结果列表，设置每个结果的 isLiked 属性
//        for (UniVo retrieval : result) {
//            boolean like = irServer.isLike(userId, retrieval.getId());
//            retrieval.setLiked(like); // 设置每个结果的点赞状态
//        }
        return Result.success(result);
    }

    @PostMapping("/deleteUsableTimes")
    public Result deleteUsableTimes(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }
        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        } catch (Exception e) {
            return Result.error("token解析出错: " + e.getMessage());
        }

        if (userId == -1) {
            return Result.error("未获取到token内信息");
        }
        User userInfo = irServer.getUserInfo(userId);
        if (userInfo.getUsableTimes() > 0) {
            irServer.deleteUsableTimes(userId);
        }
        return Result.success();
    }

    //    @GetMapping("/retrieval/getResult")
//    public Result getResult(
//            @RequestParam(value = "major", required = false) String major,
//            @RequestParam(value = "input", required = false) String input,
//            @RequestParam(value = "req", required = false) String req)
//    {
//        try {
//            List<UniRetrieval> result = irServer.getResult(major ,input ,req);
//            return Result.success(result);
//        }catch (Exception e){
//            return Result.error("服务器异常"+e.getMessage());
//        }
//    }

    @PostMapping("/like")
    public Result like(@RequestHeader("Authorization") String token, @RequestParam("id") Integer id){
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }
        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        }catch (Exception e){
            return Result.error("token解析出错: "+e.getMessage());
        }
        if (userId==-1){
            return Result.error("未获取到token内信息");
        }
        boolean like = irServer.isLike(userId, id);
        if (like){
            irServer.unLike(userId ,id);
            return Result.success("刪除成功");
        } else {
            irServer.like(userId ,id);
            return Result.success("新增成功");
        }
    }

    @GetMapping("/likeList")
    public Result getLike(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }
        String tokenV = token.substring("Bearer ".length());
        int id = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            id = (int) claims.get("id");
        }catch (Exception e){
            return Result.error("token解析出错: "+e.getMessage());
        }
        if (id==-1){
            return Result.error("未获取到token内信息");
        }
        try {
            List<LikeVo> result = irServer.getLike(id);
            return Result.success(result);
        }catch (Exception e){
            return Result.error("服务器异常"+e.getMessage());
        }
    }

    @GetMapping("/allMajors")
    public Result getAllMajors() {
        try {
            List<MajorPrimary> result = irServer.getAllMajors();
            return Result.success(result);
        }catch (Exception e){
            return Result.error("服务器异常"+e.getMessage());
        }
    }

    @PostMapping("/unlike")
    public Result unlike(@RequestHeader("Authorization") String token, @RequestParam("id") Integer id){
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }
        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        }catch (Exception e){
            return Result.error("token解析出错: "+e.getMessage());
        }
        if (userId==-1){
            return Result.error("未获取到token内信息");
        }
        irServer.unLike(userId ,id);
        return Result.success("刪除成功");
    }

    @GetMapping("/getAll")
    public Result getAll() {
        try {
            List<UniVo> result = irServer.getAll();
            return Result.success(result);
        }catch (Exception e){
            return Result.error("服务器异常"+e.getMessage());
        }
    }

    /*
     全选专业大类下的所有门类
    */
    @GetMapping("/allSelect")
    private Result allSelect(@RequestParam Integer code,@RequestParam Integer academic){
        if (code==null||academic==null){
            return Result.error("参数错误");
        }
        return Result.success(irServer.allSelect(code, academic));
    }

    /*
    * 根据所选信息年份和学校名称查询该校该年份其他招聘信息
    * */
    @GetMapping("/getOther")
    private Result getOther(@RequestParam String institution,@RequestParam String year){
        if (institution==null||year==null){
            return Result.error("参数错误");
        }
        return Result.success(irServer.getAllInUniversity(institution, year));
    }

    /*
     * 获取用户信息
     * */
    @GetMapping("/getUserInfo")
    private Result getUserInfo(@RequestHeader("Authorization") String token) {
        // 创建Logger实例
        Logger logger = LoggerFactory.getLogger(getClass());

        if (token == null || !token.startsWith("Bearer ")) {
            logger.error("Token格式错误: token为null或不符合Bearer格式");
            return Result.error("token格式错误");
        }

        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        } catch (Exception e) {
            logger.error("Token解析失败: {}", e.getMessage(), e);  // 记录详细的异常信息
            return Result.error("token解析出错: " + e.getMessage());
        }

        if (userId == -1) {
            logger.error("未获取到token内的用户信息");
            return Result.error("未获取到token内信息");
        }

        User userInfo = irServer.getUserInfo(userId);
        if (userInfo == null) {
            logger.error("未找到用户信息，userId: {}", userId);
            return Result.error("用户信息不存在");
        }

        logger.info("成功获取用户信息，userId: {}", userId);  // 记录成功日志
        return Result.success(userInfo);
    }


    /*
     * 修改密码
     * */
    @PostMapping("/updatePassword")
    private Result updatePassword(@RequestHeader("Authorization") String token, @RequestParam("password") String password){
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }
        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        }catch (Exception e){
            return Result.error("token解析出错: "+e.getMessage());
        }
        if (userId==-1){
            return Result.error("未获取到token内信息");
        }
        irServer.updatePassword(userId ,password);
        return Result.success("修改成功");
    }

    /*
     * 充值成功后逻辑代码
     * */
    @PostMapping("/updateUpTime")
    private Result updateUpTime(@RequestHeader("Authorization") String token, @RequestParam("tradeNo") String tradeNo){
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("token格式错误");
        }
        String tokenV = token.substring("Bearer ".length());
        int userId = -1;
        try {
            Claims claims = JwtUtils.parseJWT(tokenV);
            userId = (int) claims.get("id");
        }catch (Exception e){
            return Result.error("token解析出错: "+e.getMessage());
        }
        if (userId==-1){
            return Result.error("未获取到token内信息");
        }
        irServer.updateUpTime(userId);
//        irServer.deleteOldVips(userId);
//        irServer.increaseVipsSucc(userId);
        irServer.updateVipsSucc(userId, tradeNo);
        return Result.success("修改成功");
    }


}
