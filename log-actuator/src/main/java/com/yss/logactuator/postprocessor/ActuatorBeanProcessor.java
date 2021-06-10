package com.yss.logactuator.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;

import java.util.Set;

public class ActuatorBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof WebEndpointProperties){
            WebEndpointProperties properties = (WebEndpointProperties)bean;
            Set<String> include = properties.getExposure().getInclude();
            include.add("metrics");
            include.add("host-memory");
            properties.getExposure().setInclude(include);
        }
        return bean;
    }
}
