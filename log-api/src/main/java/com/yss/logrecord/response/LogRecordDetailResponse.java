package com.yss.logrecord.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 描述：日志记录详情
 *
 * @author wangliang at 2020/5/21 18:00
 * @version 1.0.0
 */
@Data
@ApiModel("日志记录详情")
public class LogRecordDetailResponse implements Serializable {
    @ApiModelProperty("请求地址")
    private String url;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("请求开始时间")
    private LocalDateTime startTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("返回时间")
    private LocalDateTime returnTime;
    @ApiModelProperty("请求时间")
    private long timeConsuming;
    @ApiModelProperty("浏览器")
    private String browser;
    @ApiModelProperty("登录的token")
    private String refreshtoken;
}
