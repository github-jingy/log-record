package com.yss.logrecord.domain;

import com.yss.logrecord.gateways.acl.constant.CommonConstant;
import com.yss.logrecord.gateways.acl.factory.MemoryInfoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemoryInfoService {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    @Qualifier("getMemoryPool")
    private ThreadPoolExecutor pool;

    public MemoryInfoDo memoryInfo(){
        Map<String, String> services = getServices();
//        return collectStatistics(services);
        return calculate(getAllInfo(services));
    }

    private MemoryInfoDo collectStatisticsSingle(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://192.168.98.37:9504" + CommonConstant.MEMORY_URI, Map.class);
        if(forEntity.getStatusCodeValue() == 200){
            Map body = forEntity.getBody();
            String total = String.valueOf(body.get(CommonConstant.TOTAL));
            String available = String.valueOf(body.get(CommonConstant.AVAILABLE));
            return MemoryInfoFactory.create(Double.parseDouble(total),Double.parseDouble(available));
        }else {
            throw new RuntimeException("没有找到相关路径");
        }
    }

    private Map<String,String> getServices(){
        List<String> services = client.getServices();
        Map<String,String> unique = new HashMap<>();
        services.stream().forEach(item ->{
            if(!serviceName.equals(item)){
                List<ServiceInstance> instances = client.getInstances(item);
                if(!CollectionUtils.isEmpty(instances)){
                    instances.forEach(instance -> unique.put(instance.getHost(),instance.getUri().toString()));
                }
            }
        });
        return unique;
    }

    //-------------------------第一版------------------------------//

    private MemoryInfoDo collectStatistics(Map<String,String> data){
        double total = 0.0;
        double available = 0.0;
        for (Map.Entry<String,String> entry:data.entrySet()){
            ResponseEntity<Map<String,String>> forEntity = getForEntity(entry.getValue());
            if(forEntity != null && forEntity.getStatusCodeValue() == 200){
                Map<String,String> body = forEntity.getBody();
                String totalItem = body.get(CommonConstant.TOTAL);
                String availableItme = body.get(CommonConstant.AVAILABLE);
                if(StringUtils.hasText(totalItem) && StringUtils.hasText(availableItme)){
                    total = total + Double.parseDouble(totalItem);
                    available = available + Double.parseDouble(availableItme);
                }
            }
        }
        return MemoryInfoFactory.create(total,available);
    }

    private ResponseEntity<Map<String,String>> getForEntity(String url){
        ResponseEntity<Map<String,String>> forEntity = new ResponseEntity(HttpStatus.OK);
        try {
            ResponseEntity<Map> entity = restTemplate.getForEntity(url + CommonConstant.MEMORY_URI, Map.class);
            return new ResponseEntity<Map<String,String>>(entity.getBody(),entity.getStatusCode());
        }catch (Exception e){
            //ignore exception
            log.error(String.format("request %s exception message %s cause %s",url+CommonConstant.MEMORY_URI,e.getMessage(),e.getCause()));
        }
        return forEntity;

    }

    //------------------------------第二版------------------------------//
    private MemoryInfoDo calculate(List<Map<String,String>> data){
        if(CollectionUtils.isEmpty(data)){
            return MemoryInfoFactory.create(0.00,0.00);
        }
        Double total = data.stream().filter(filter ->{
            if(filter != null && filter.containsKey(CommonConstant.TOTAL)){
                return true;
            }
            return false;
        }).mapToDouble(item ->Double.parseDouble(item.get(CommonConstant.TOTAL))).sum();
        Double available = data.stream().filter(filter ->{
            if(filter != null && filter.containsKey(CommonConstant.AVAILABLE)){
                return true;
            }
            return false;
        }).mapToDouble(item ->Double.parseDouble(item.get(CommonConstant.AVAILABLE))).sum();
        return MemoryInfoFactory.create(total,available);
    }

    private List<Map<String,String>> getAllInfo(Map<String,String> data){
        Collection<String> values = data.values();
        List<CompletableFuture<ResponseEntity<Map<String,String>>>> collect = values.stream().map(item -> {
            String value = item;
            return CompletableFuture.supplyAsync(() -> getForEntity(value),pool);
        }).collect(Collectors.toList());
        List<ResponseEntity<Map<String,String>>> result = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
        return result.stream().filter(filter ->{
            if(filter.getStatusCodeValue() == 200){
                return true;
            }
            return false;
        }).map(item ->item.getBody()).collect(Collectors.toList());
    }

    //--------------------------第三版----------------------------------//
    private List<Map<String,String>> getAll(Map<String,String> data){
        List<Future<Map<String, String>>> submits = new ArrayList<>();
        for(Map.Entry<String,String> entry:data.entrySet()){
            Future<Map<String, String>> submit = pool.submit(() -> {
                ResponseEntity<Map<String, String>> forEntity = getForEntity(entry.getValue());
                if (forEntity.getStatusCodeValue() == 200) {
                    return forEntity.getBody();
                }
                return Collections.emptyMap();
            });
            submits.add(submit);
        }
        List<Map<String, String>> collect = submits.stream().map(item -> {
            try {
                return item.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return new HashMap<String,String>();
        }).collect(Collectors.toList());
        return collect;
    }
}
