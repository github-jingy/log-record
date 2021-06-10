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

@Data
@ApiModel("日志返回对象")
public class LogEntityResponse implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("客户端IP")
    private String clientIp;

    @ApiModelProperty("类名称")
    private String className;

    @ApiModelProperty("类方法")
    private String classMethod;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求地址")
    private String url;

    @ApiModelProperty("请求类型")
    private String reqType;

    @ApiModelProperty("方法描述")
    private String swaggerApiOperation;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("请求开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("请求数据")
    private String requestData;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("返回时间")
    private LocalDateTime returnTime;

    @ApiModelProperty("返回数据")
    private String responseData;

    @ApiModelProperty("返回状态码")
    private Integer httpStatusCode;

    @ApiModelProperty("请求时间")
    private long timeConsuming;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户编码")
    private String userCode;

    @ApiModelProperty("系统ID")
    private String fSysId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户主机")
    private String userHost;

    @ApiModelProperty("当前服务名称")
    private String serviceName;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("类描述")
    private String swaggerApi;

    @ApiModelProperty("刷新Token")
    private String refreshtoken;

    @ApiModelProperty("创建时间")
    private LocalDateTime createtime;


    @ApiModelProperty("业务操作说明")
    private String info;

    @ApiModelProperty("返回状态码名称")
    private String httpStatusName;

    public void returnName(){
        if(httpStatusCode!=null && httpStatusCode==200){
            this.setHttpStatusName("成功");
        }else{
            this.setHttpStatusName("失败");
        }
    }
}