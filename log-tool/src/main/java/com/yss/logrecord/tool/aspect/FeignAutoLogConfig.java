package com.yss.logrecord.tool.aspect;

import com.yss.logrecord.tool.feign.FeignProperties;
import com.yss.logrecord.tool.feign.StoreLogFeign;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(FeignClientsConfiguration.class)//默认提供的配置类
@Configuration
@EnableConfigurationProperties(FeignProperties.class)
public class FeignAutoLogConfig {

    @Autowired
    private FeignProperties feignProperties;

    @Bean
    public StoreLogFeign getStoreLogFeign(Decoder decoder, Encoder encoder, Client client, Contract contract){
        return Feign.builder().client(client).decoder(decoder).encoder(encoder).contract(contract)
                .target(StoreLogFeign.class,feignProperties.getUrl());
    }
}
