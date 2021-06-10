package com.yss.logrecord.tool.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yss.logrecord.request.LogEntityRequest;
import com.yss.logrecord.tool.feign.StoreLogFeign;
import com.yss.logrecord.tool.persistence.PersistenceLog;
import com.yss.logrecord.tool.persistence.PersistenceLogFeign;
import com.yss.logrecord.tool.utils.DateUtil;
import com.yss.logrecord.tool.utils.LogUtil;
import com.yss.logrecord.tool.utils.WebUtil;
import com.yss.logrecord.tool.vo.RequestData;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Order(1)
@Slf4j
@Configuration
@EnableAsync
public class WebLogAspect implements ApplicationContextAware {

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private StoreLogFeign storeLogFeign;

    ThreadLocal<LogEntityRequest> logLocal = new ThreadLocal<>();

    public WebLogAspect(){

    }

    @Pointcut("(@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping))" +
            "&& !execution(* com.yss.logrecord.tool.feign.*.*(..)) && !execution(* *..feign..*.*(..)) && !execution(* *..api..*.*(..)))")
    public void logPointCut(){

    }

    @Before("logPointCut()")
    public void before(JoinPoint joinPoint){
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            HttpServletRequest request = WebUtil.getRequest();
            Class<?> aClass = Class.forName(className);
            LogEntityRequest entity = new LogEntityRequest();
            LogUtil.getAnnClass(aClass,entity,className);
            LogUtil.getAnnMethod(aClass,methodName,entity);
            LogUtil.getHeader(request,entity);
            RequestData requestData = RequestData.builder()
                    .header(LogUtil.getHeaders(request))
                    .param(LogUtil.getParams(request))
                    .args(LogUtil.getArgs(joinPoint)).build();
            entity.setRequestData(requestData.toString());
            LogUtil.setRequest(request,entity);
            logLocal.set(entity);
        } catch (Exception e){
            log.error("web log before error message {} case {}",e.getMessage(),e.getCause());
            //ignore exception
        }

    }


    @AfterReturning(value = "logPointCut()",returning = "returnData")
    public void afterReturn(JoinPoint joinPoint,Object returnData){
        try {
            HttpServletResponse response = WebUtil.getResponse();
            LogEntityRequest entity = logLocal.get();
            entity.setReturnTime(DateUtil.currentTime());
            entity.setTimeConsuming(DateUtil.between(entity.getStartTime(),DateUtil.currentTime()));
            entity.setHttpStatusCode(response.getStatus());
            entity.setServiceName(serviceName);
            entity.setResponseData(JSON.toJSONString(returnData,
                    new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue}));
            storelog(entity);
        } catch (Exception e){
            log.error("web log afterreturn error message {} case {}",e.getMessage(),e.getCause());
            //ignore exception
        } finally {
            logLocal.remove();
        }

    }

    private void storelog(LogEntityRequest request){
        PersistenceLog bean = applicationContext.getBean(PersistenceLog.class);
        bean.storeLog(request);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Bean
    @ConditionalOnMissingBean(PersistenceLog.class)
    public PersistenceLog getPersistence(){
        PersistenceLogFeign feign = new PersistenceLogFeign(storeLogFeign);
        return feign;
    }

}
