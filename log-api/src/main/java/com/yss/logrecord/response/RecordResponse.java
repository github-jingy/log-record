package com.yss.logrecord.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录统计")
public class RecordResponse {

    @ApiModelProperty("时间")
    private String time;


    @ApiModelProperty("数量")
    private String count;
}
