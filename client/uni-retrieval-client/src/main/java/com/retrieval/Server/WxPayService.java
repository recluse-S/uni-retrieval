package com.retrieval.Server;

import com.retrieval.pojo.ApiResponseBody;
import com.retrieval.pojo.Vo.WxPayOrderReqVO;

import javax.servlet.http.HttpServletRequest;

public interface WxPayService {

    ApiResponseBody createOrder(WxPayOrderReqVO req, HttpServletRequest request) throws Exception;

    ApiResponseBody payNotify(HttpServletRequest request) throws Exception;

    ApiResponseBody queryPayOrder(String tradeNo);

    Integer getAmount();
}
