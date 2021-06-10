package com.yss.logactuator.config;

import com.yss.logactuator.endpoint.HostMemoryEndPoint;
import com.yss.logactuator.postprocessor.ActuatorBeanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ActuatorConfig {

    @Bean
    public ActuatorBeanProcessor actuatorBeanProcessor(){
        return new ActuatorBeanProcessor();
    }

    @Bean
    public HostMemoryEndPoint hostMemoryEndPoint(){
        return new HostMemoryEndPoint();
    }


}
