package com.yss.logrecord.application;

import com.yss.logrecord.core.gateways.utils.BeanUtils;
import com.yss.logrecord.domain.*;
import com.yss.logrecord.request.LogByUrlAndTimeRequest;
import com.yss.logrecord.request.LogRecordDetailRequest;
import com.yss.logrecord.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryLogAppService {

    @Autowired
    private MemoryInfoService memoryInfo;

    private final QueryLogService queryLogService;

    public QueryLogAppService(QueryLogService queryLogService){
        this.queryLogService = queryLogService;
    }

    public List<RecordResponse> statisticsOfLogin(String dateTime){
        List<RecordDo> recordDos = queryLogService.statisticsOfLogin(dateTime);
        return BeanUtils.copyPropertiesOfList(recordDos,RecordResponse.class);
    }

    public List<BrowserResponse> statisticsOfBrowser(){
        List<BrowserDo> browserDos = queryLogService.statisticsOfBrowser();
        return BeanUtils.copyPropertiesOfList(browserDos,BrowserResponse.class);
    }

    /**
     * 描述: 查询指定时间段的日志记录
     *
     * @param request request
     * @return List<LogRecordDetailResponse>
     * @author wangliang at 2020/5/21 18:16
     */
    public List<LogRecordDetailResponse> findAllByStartTimeBetween(LogRecordDetailRequest request) {
        return queryLogService.findAllByStartTimeBetween(request.getStartTime(), request.getEndDate());

    }

    /**
     * 描述: 通过url和请求时间查询
     *
     * @param request 请求体
     * @return 日志集合
     * @author wangliang at 2020/5/25 14:18
     */
    public List<LogRecordDetailResponse> findByUrlAnTimeBetween(LogByUrlAndTimeRequest request) {
        List<LogRecordDetailResponse> responses = new ArrayList<>();
        if (!StringUtils.isEmpty(request.getUrl())) {
            request.setUrl("%" + request.getUrl() + "%");
            responses = queryLogService.findAllByUrlLikeAndStartTimeBetweenOrderByStartTimeAsc(request.getUrl(), request.getStartTime(), request.getEndDate());
        }

        return responses;
    }

    public MemoryInfoResponse memoryInfo(){
        MemoryInfoDo memoryInfoDo = memoryInfo.memoryInfo();
        return BeanUtils.copyProperties(memoryInfoDo,MemoryInfoResponse.class);
    }

    /**
     * 查询业务日志
     * @param name
     * @param begin
     * @param end
     * @param page
     * @param limit
     * @return
     */
    public PageDataLogDO<LogEntityResponse> queryBusinessLogList(String name, String user, String begin, String end, Integer page, Integer limit){
        return queryLogService.queryBusinessLogList(name, user, begin, end, page, limit);
    }
}