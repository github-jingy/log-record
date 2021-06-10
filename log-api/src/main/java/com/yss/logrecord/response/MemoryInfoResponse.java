package com.yss.logrecord.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("内存详细")
public class MemoryInfoResponse {

    @ApiModelProperty("总内存")
    private String total;


    @ApiModelProperty("可用内存")
    private String available;
}
