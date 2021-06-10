package com.yss.logrecord.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yss.logrecord.core.gateways.utils.DateUtils;
import com.yss.logrecord.gateways.acl.constant.CommonConstant;
import com.yss.logrecord.gateways.acl.factory.BrowserFactory;
import com.yss.logrecord.gateways.acl.factory.RecordFactory;
import com.yss.logrecord.response.LogEntityResponse;
import com.yss.logrecord.response.LogRecordDetailResponse;
import com.yss.logrecord.response.PageDataLogDO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class QueryLogService {

    @Autowired
    private LogRecordRepository logRecord;

    public List<RecordDo> statisticsOfLogin(String dateTime){
        List<LogEntityDo> logByTime = getLogByTime();
        if(CollectionUtils.isEmpty(logByTime)){
            return Collections.emptyList();
        }
        List<RecordDo> result = new ArrayList<>();
        List<LocalDateTime> localDateTimes = DateUtils.onPointTimeMinute(dateTime);
        localDateTimes.stream().forEach(item ->{
            long count = filterByDate(logByTime, item);
            result.add(RecordFactory.create(item,count));
        });
        return result;
    }

    private List<LogEntityDo> getLogByTime(){
        LocalDateTime startTime = DateUtils.startOfTheDay();
        return logRecord.findAllByUrlLikeAndStartTimeBetween(CommonConstant.LOGIN,startTime,DateUtils.currentDateTime());
    }

    private long filterByDate(List<LogEntityDo> list,LocalDateTime dateTime){
        LocalDateTime endTime = DateUtils.pushBackMinute(dateTime, 1);
        return list.stream().filter(item ->{
            LocalDateTime startTime = item.getStartTime();
            if(startTime.isAfter(dateTime) && startTime.isBefore(endTime)){
                return true;
            }
            if(startTime.equals(dateTime)){
                return true;
            }
            return false;
        }).count();
    }

    public List<BrowserDo> statisticsOfBrowser(){
        List result = logRecord.findAllByGroupAndBrowsers();
        if(CollectionUtils.isEmpty(result)){
            return Collections.emptyList();
        }
        List<BrowserDo> collect = new ArrayList<>();
        result.forEach(row ->{
            Object[] cell = (Object[])row;
            collect.add(BrowserFactory.create(cell));
        });
        return collect;
    }

    /**
     * 描述: 查询指定时间段的请求
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<LogEntityDo>
     * @author wangliang at 2020/5/21 17:34
     */
    public List<LogRecordDetailResponse> findAllByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<LogRecordDetailResponse> list = new ArrayList<>();
        List<Map<String, Object>> mapList = logRecord.findByStartTime(startTime, endTime);
        String jsonString = JSON.toJSONString(mapList);
        list = JSONArray.parseArray(jsonString, LogRecordDetailResponse.class);
        return list;
    }

    /**
     * 描述: 查询指定时间区间内指定模块的日志记录
     *
     * @param url url
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<LogEntityDo>
     * @author wangliang at 2020/5/25 14:10
     */
    public List<LogRecordDetailResponse> findAllByUrlLikeAndStartTimeBetweenOrderByStartTimeAsc(String url,LocalDateTime startTime, LocalDateTime endTime) {
        List<LogRecordDetailResponse> list = new ArrayList<>();
        if (startTime != null && endTime == null) {
            List<Map<String, Object>> mapList = logRecord.findByUrLAndTimeOrderByTime(url, startTime, endTime);
            String jsonString = JSON.toJSONString(mapList);
            list = JSONArray.parseArray(jsonString, LogRecordDetailResponse.class);
            return list;
        } else {
            List<Map<String, Object>> maps = logRecord.findByUrlOrderByTimeAsc(url);
            String jsonString = JSON.toJSONString(maps);
            list = JSONArray.parseArray(jsonString, LogRecordDetailResponse.class);
            return list;
        }
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
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "returnTime");
        Specification<LogEntityDo> specification = new Specification<LogEntityDo>() {
            @Override
            public Predicate toPredicate(Root<LogEntityDo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                Predicate program=null;
                if(StringUtils.isNotBlank(name)){
                    program =criteriaBuilder.and(criteriaBuilder.like(root.get("swaggerApiOperation"),"%"+name+"%"));
                    predicates.add(program);
                }
                if(StringUtils.isNotBlank(user)){
                    program =criteriaBuilder.and(criteriaBuilder.like(root.get("userName"),"%"+user+"%"));
                    predicates.add(program);
                }
                if(StringUtils.isNotBlank(begin)){
                    program =criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("returnTime"),LocalDateTime.parse(begin+" 00:00:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));//数据创建时间大于等于开始时间
                    predicates.add(program);
                }
                if(StringUtils.isNotBlank(end)){
                    program =criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("returnTime"),LocalDateTime.parse(end+" 23:59:59",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));//数据创建时间小于等于结束时间
                    predicates.add(program);
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
        Page<LogEntityDo> pageTemplate= logRecord.findAll(specification,pageable);
        List<LogEntityResponse> returnList=new ArrayList();
        List<LogEntityDo> pageTemplateList = pageTemplate.getContent();
        LogEntityResponse logEntityResponse=null;
        for(LogEntityDo po:pageTemplateList){
            logEntityResponse=new LogEntityResponse();
            BeanUtils.copyProperties(po, logEntityResponse);
            logEntityResponse.returnName();
            returnList.add(logEntityResponse);
        }
        return new PageDataLogDO(pageTemplate.getTotalElements(), returnList);
    }
}