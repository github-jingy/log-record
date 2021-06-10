package com.yss.logrecord.api;

import com.yss.common.result.ResponseResult;
import com.yss.logrecord.request.LogEntityRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = "日志存储")
public interface StoreLogApi {

    @ApiOperation("日志添加")
    @PostMapping("/store-log/add")
    ResponseResult<LogEntityRequest> add(@RequestBody LogEntityRequest request);

    @ApiOperation("业务日志添加")
    @PostMapping("/store-log/add-business")
    ResponseResult<LogEntityRequest> addBusiness(@RequestBody LogEntityRequest request);
}
