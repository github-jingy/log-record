package com.yss.logrecord.gateways.acl.factory;

import com.yss.logrecord.domain.BrowserDo;

public class BrowserFactory {

    public static BrowserDo create(Object[] cell){

        return BrowserDo.builder()
                .code(String.valueOf(cell[0]))
                .num(Long.parseLong(String.valueOf(cell[1]))).build();
    }
}
