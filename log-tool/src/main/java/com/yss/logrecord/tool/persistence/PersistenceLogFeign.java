package com.yss.logrecord.tool.persistence;

import com.yss.logrecord.request.LogEntityRequest;
import com.yss.logrecord.tool.feign.StoreLogFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class PersistenceLogFeign implements PersistenceLog {

    private StoreLogFeign feign;

    public PersistenceLogFeign(StoreLogFeign feign){
        this.feign = feign;
    }

    @Async
    @Override
    public void storeLog(LogEntityRequest entiry) {
        try {
            feign.add(entiry);
        }catch (Exception e){
            //ignore exception
            log.error("feign add exception message {}",e.getMessage());
        }

    }
}
