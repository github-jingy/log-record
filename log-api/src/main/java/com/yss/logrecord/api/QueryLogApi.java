package com.yss.logrecord.api;

import com.yss.common.result.ResponseResult;
import com.yss.logrecord.request.LogByUrlAndTimeRequest;
import com.yss.logrecord.request.LogRecordDetailRequest;
import com.yss.logrecord.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Api(tags = "日志查询")
public interface QueryLogApi {

    @ApiOperation("登录信息统计")
    @GetMapping("/query-log/statistics-login")
    ResponseResult<List<RecordResponse>> statisticsOfLogin(@RequestParam("startTime") String dateTime);

    @ApiOperation("查询指定时间段的日志详情")
    @PostMapping("/query-log/logrecord-detail")
    ResponseResult<List<LogRecordDetailResponse>> getLogDetailByTimeBetween(@RequestBody LogRecordDetailRequest request);

    /**
     * 描述: 查询指定路径时间段的日志详情
     *
     * @param request 指定时间段
     * @return 日志详情
     * @author wangliang at 2020/5/21 18:22
     */
    @ApiOperation("查询指定路径时间段的日志详情")
    @PostMapping("/query-log/logurltime-detail")
    ResponseResult<List<LogRecordDetailResponse>> findByUrlAnTimeBetween(@RequestBody LogByUrlAndTimeRequest request);

    @ApiOperation("内存信息")
    @GetMapping("/query-log/memory-info")
    ResponseResult<MemoryInfoResponse> memoryInfo();

    @ApiOperation("浏览器信息统计")
    @GetMapping("/query-log/statistics-browser")
    ResponseResult<List<BrowserResponse>> statisticsOfBrowser();

    @ApiOperation("查询业务日志")
    @GetMapping("/query-log/business")
    ResponseResult<PageDataLogDO<LogEntityResponse>> queryBusinessLogList(@RequestParam(value="name",required = false) String name,
                                                                          @RequestParam(value="user",required = false) String user,
                                                                          @RequestParam(value="begin",required = false) String begin,
                                                                          @RequestParam(value="end",required = false) String end,
                                                                          @RequestParam(value="page",required = false) Integer page,
                                                                          @RequestParam(value="limit",required = false) Integer limit);
}