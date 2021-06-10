package com.yss.logrecord.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoreLogService {

    @Autowired
    private LogRecordRepository logRecordRepository;


    public LogEntityDo add(LogEntityDo entityDo){
        entityDo.setId(getUUID());
        return logRecordRepository.save(entityDo);
    }

    /**
     * 获取UUID
     * @return
     */
    private String getUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

}
