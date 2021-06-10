package com.yss.logrecord.gateways.ohs;

import com.yss.common.result.ResponseResult;
import com.yss.logrecord.application.QueryLogAppService;
import com.yss.logrecord.core.gateways.ohs.Resources;
import com.yss.logrecord.request.LogByUrlAndTimeRequest;
import com.yss.logrecord.request.LogRecordDetailRequest;
import com.yss.logrecord.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "查询日志")
@RestController
@RequestMapping("/query-log")
public class QueryLogResource {

    private final QueryLogAppService queryLogAppService;

    public QueryLogResource(QueryLogAppService queryLogAppService){
        this.queryLogAppService = queryLogAppService;
    }

    @ApiOperation("登录信息统计")
    @GetMapping("/statistics-login")
    public ResponseResult<List<RecordResponse>> statisticsOfLogin(@RequestParam(value = "startTime") String dateTime){
        return Resources.with("statisticsOfLogin")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->queryLogAppService.statisticsOfLogin(dateTime));
    }

    @ApiOperation("浏览器信息统计")
    @GetMapping("/statistics-browser")
    public ResponseResult<List<BrowserResponse>> statisticsOfBrowser(){
        return Resources.with("statisticsOfBrowser")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->queryLogAppService.statisticsOfBrowser());
    }

    /**
     * 描述: 查询指定时间段的日志详情
     *
     * @param request 指定时间段
     * @return 日志详情
     * @author wangliang at 2020/5/21 18:22
     */
    @ApiOperation("查询指定时间段的日志详情")
    @PostMapping("/logrecord-detail")
    public ResponseResult<List<LogRecordDetailResponse>> getLogDetailByTimeBetween(@RequestBody LogRecordDetailRequest request) {
        return Resources.with("getLogDIetailByTimeBetween")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->queryLogAppService.findAllByStartTimeBetween(request));

    }
    /**
     * 描述: 查询指定路径时间段的日志详情
     *
     * @param request 指定时间段
     * @return 日志详情
     * @author wangliang at 2020/5/21 18:22
     */
    @ApiOperation("查询指定路径时间段的日志详情")
    @PostMapping("/logurltime-detail")
    public ResponseResult<List<LogRecordDetailResponse>> findByUrlAnTimeBetween(@RequestBody LogByUrlAndTimeRequest request) {
        return Resources.with("getLogDIetailByTimeBetween")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->queryLogAppService.findByUrlAnTimeBetween(request));

    }


    @ApiOperation("内存信息")
    @GetMapping("/memory-info")
    public ResponseResult<MemoryInfoResponse> memoryInfo(){
        return Resources.with("memoryInfo")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->queryLogAppService.memoryInfo());
    }

    @ApiOperation("查询业务日志")
    @GetMapping("/business")
    ResponseResult<PageDataLogDO<LogEntityResponse>> queryBusinessLogList(@RequestParam(value="name",required = false) String name,
                                                                          @RequestParam(value="user",required = false) String user,
                                                                          @RequestParam(value="begin",required = false) String begin,
                                                                          @RequestParam(value="end",required = false) String end,
                                                                          @RequestParam(value="page",required = false) Integer page,
                                                                          @RequestParam(value="limit",required = false) Integer limit){
        return Resources.with("queryBusinessLogList")
                .onSuccess(HttpStatus.OK)
                .onError(HttpStatus.BAD_REQUEST)
                .onFailed(HttpStatus.INTERNAL_SERVER_ERROR)
                .execute(() ->queryLogAppService.queryBusinessLogList(name, user, begin, end, page, limit));
    }
}