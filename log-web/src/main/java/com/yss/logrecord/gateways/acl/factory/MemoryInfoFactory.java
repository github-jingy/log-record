package com.yss.logrecord.gateways.acl.factory;

import com.yss.logrecord.domain.MemoryInfoDo;

public class MemoryInfoFactory {

    public static MemoryInfoDo create(double total,double available){
        return MemoryInfoDo.builder()
                .total(String.valueOf(total))
                .available(String.valueOf(available))
                .build();
    }
}
