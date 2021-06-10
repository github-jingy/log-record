package com.yss.logactuator.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "host-memory")
public class HostMemoryEndPoint {

    @ReadOperation
    public Map<String,Object> getHostMemoryInfo(){
        return getMemoryInfo();
    }

    private Map<String,Object> getMemoryInfo(){
        Map<String,Object> data = new HashMap<>();
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long total = memory.getTotal();
        //可用内存
        long available = memory.getAvailable();
        data.put("total",convetByte2MB(total));
        data.put("available",convetByte2MB(available));
        data.put("unit","MB");
        return data;
    }

    private String convetByte2MB(long num){
        BigDecimal decimal = new BigDecimal(Long.toString(num));
        int format = 1024 * 1024;
        return decimal.divide(new BigDecimal(format),2,BigDecimal.ROUND_HALF_UP).toString();
    }



}
