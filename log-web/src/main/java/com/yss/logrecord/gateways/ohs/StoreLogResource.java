package com.yss.logrecord.gateways.ohs;

import com.yss.common.result.ResponseResult;
import com.yss.logrecord.application.StoreLogAppService;
import com.yss.logrecord.core.gateways.ohs.Resources;
import com.yss.logrecord.request.LogEntityRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "存储日志")
@RestController
@RequestMapping("/store-log")
public class StoreLogResource {

    private final StoreLogAppService storeLogAppService;

    public StoreLogResource(StoreLogAppService storeLogAppService){
        this.storeLogAppService = storeLogAppService;
    }

    @ApiOperation("存储日志信息")
    @PostMapping("/add")
    public ResponseResult<LogEntityRequest> add(@RequestBody LogEntityRequest request){
        return Resources.with("add")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->storeLogAppService.add(request));
    }

    @ApiOperation("存储业务日志信息")
    @PostMapping("/add-business")
    public ResponseResult<LogEntityRequest> addBusiness(@RequestBody LogEntityRequest request){
        return Resources.with("addBusiness")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->storeLogAppService.addBusiness(request));
    }
}