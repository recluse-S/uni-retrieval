package com.retrieval.controller;

import com.retrieval.Server.IRServer;
import com.retrieval.Server.WxPayService;
import com.retrieval.pojo.ApiResponseBody;
import com.retrieval.pojo.JwtUtils;
import com.retrieval.pojo.Result;
import com.retrieval.pojo.Vo.UniVo;
import com.retrieval.pojo.Vo.WxPayOrderReqVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(value = "微信-支付", tags = {"微信-支付"})
@RestController
@RequestMapping("wechat/pay")
public class WxPayController{

    @Resource
    private WxPayService wxPayService;

    @GetMapping("/getAmount")
    public Result getAmount() {
        Integer result = wxPayService.getAmount();
        return Result.success(result);
    }

    /**
     * 微信预支付
     * @param req
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信预支付", notes = "微信预支付")
    @PostMapping("/createOrder")
    public ApiResponseBody createOrder(@RequestBody @Validated WxPayOrderReqVO req, HttpServletRequest request
                    ) throws Exception {
        return wxPayService.createOrder(req, request);
    }

    /**
     * 微信支付回调
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信支付回调", notes = "微信支付回调")
    @PostMapping("/payNotify")
    public ApiResponseBody payNotify(HttpServletRequest request) throws Exception {
        log.info("-----------------------微信支付回调通知-----------------------");
        return  wxPayService.payNotify(request);
    }

    /**
     * 查询支付
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询支付", notes = "查询支付")
    @GetMapping("/queryPayOrder")
    public ApiResponseBody queryPayOrder(@RequestParam("tradeNo") String tradeNo) {
        log.info("-----------------------订单号：" + tradeNo);
        if (tradeNo == null || "".equals(tradeNo)) {
            return ApiResponseBody.error("微信支付异常，参数错误");
        }
        return  wxPayService.queryPayOrder(tradeNo);
    }
}
