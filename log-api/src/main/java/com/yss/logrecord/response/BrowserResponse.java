package com.yss.logrecord.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("浏览器信息")
public class BrowserResponse {

    /**
     * 浏览器类型
     */
    private String code;

    /**
     * 浏览器数量
     */
    private long num;
}
