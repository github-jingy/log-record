package com.yss.logrecord.gateways.acl.factory;

import com.yss.logrecord.core.gateways.utils.DateUtils;
import com.yss.logrecord.domain.RecordDo;

import java.time.LocalDateTime;

public class RecordFactory {

    public static RecordDo create(LocalDateTime time,long count){
        return RecordDo.builder()
                .time(DateUtils.formatDateTime(time))
                .count(String.valueOf(count)).build();
    }
}
