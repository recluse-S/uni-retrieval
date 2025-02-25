package com.retrieval.pojo.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class WxPayOrderReqVO {

    @ApiModelProperty(value = "订单支付类型(商品订单;预约订单)", required = true)
    @NotBlank(message = "订单支付类型不能为空！")
    private String orderType;//附加数据，回调时可根据这个数据辨别订单类型或其他

    @ApiModelProperty(value = "总金额(单位：分)", required = true)
    @NotNull(message = "总金额不能为空！")
    private BigDecimal amount;

    @ApiModelProperty(value = "商品描述", required = true)
    @NotBlank(message = "商品描述不能为空！")
    private String description;

}