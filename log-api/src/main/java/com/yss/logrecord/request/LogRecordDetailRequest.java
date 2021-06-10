package com.yss.logrecord.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 描述：通过开始时间和结束查询日志详情请求体
 *
 * @author wangliang at 2020/5/21 18:03
 * @version 1.0.0
 */
@Data
@ApiModel("通过开始时间和结束查询日志详情请求体")
public class LogRecordDetailRequest implements Serializable {

    /** 开始时间 */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @ApiModelProperty("结束时间")
    private LocalDateTime endDate;
}
