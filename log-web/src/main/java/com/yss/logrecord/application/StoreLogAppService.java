package com.yss.logrecord.application;

import com.yss.logrecord.core.gateways.utils.BeanUtils;
import com.yss.logrecord.domain.LogEntityDo;
import com.yss.logrecord.domain.StoreLogService;
import com.yss.logrecord.request.LogEntityRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StoreLogAppService {

    private final StoreLogService storeLogService;

    @Value("${log.print:false}")
    private Boolean logPrint;

    public StoreLogAppService(StoreLogService storeLogService){
        this.storeLogService = storeLogService;
    }

    public LogEntityRequest add(LogEntityRequest request){
        LogEntityRequest logEntityRequest = new LogEntityRequest();
        if(logPrint) {
            LogEntityDo entityDo = BeanUtils.copyProperties(request, LogEntityDo.class);
            LogEntityDo add = storeLogService.add(entityDo);
            logEntityRequest = BeanUtils.copyProperties(add, LogEntityRequest.class);
        }
        return logEntityRequest;
    }

    public LogEntityRequest addBusiness(LogEntityRequest request){
        LogEntityDo entityDo = BeanUtils.copyProperties(request, LogEntityDo.class);
        LogEntityDo add = storeLogService.add(entityDo);
        LogEntityRequest logEntityRequest = BeanUtils.copyProperties(add, LogEntityRequest.class);
        return logEntityRequest;
    }
}
