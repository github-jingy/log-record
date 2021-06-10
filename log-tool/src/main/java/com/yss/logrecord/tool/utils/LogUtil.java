package com.yss.logrecord.tool.utils;

import com.yss.logrecord.request.LogEntityRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private static final String REFRESH_TOKEN = "refreshtoken";


    public static String getCliectIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String[] arr = ip.split(",");
        String[] var3 = arr;
        int var4 = arr.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }

        return ip;
    }


    public static String getRequestType(HttpServletRequest request) {
        String type = request.getHeader("X-Requested-With");
        return StringUtils.isEmpty(type) ? "普通请求" : "X-Requested-With:" + type;
    }

    public static void getAnnClass(Class<?> className, LogEntityRequest entity,String aClass) {
        entity.setClassName(aClass);
        if (StringUtils.isEmpty(className)) {
            return;
        } else {
            Annotation[] annotations = className.getAnnotations();
            if (annotations.length == 0) {
                return;
            } else {
                Api api = className.getAnnotation(Api.class);
                if (StringUtils.isEmpty(api)) {
                    return;
                } else {
                    String description = api.description();
                    if(description == null){
                        description = api.tags() != null?api.tags()[0]:null;
                    }
                    entity.setSwaggerApi(description);
                }
            }
        }
    }

    public static void getAnnMethod(Class<?> className, String methodName, LogEntityRequest entity) {
        entity.setClassMethod(methodName);
        if (StringUtils.isEmpty(className) || StringUtils.isEmpty(methodName)) {
            return;
        }
        Method[] methods = className.getMethods();
        Method[] var4 = methods;
        int var5 = methods.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            if (method.getName().equals(methodName)) {
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                if (StringUtils.isEmpty(apiOperation)) {
                    return;
                }
                entity.setSwaggerApiOperation(apiOperation.value());
            }
        }

    }

    public static void getHeader(HttpServletRequest request, LogEntityRequest logger) throws UnsupportedEncodingException {
        logger.setUserId(StringUtils.isEmpty(request.getHeader("userId")) ? "userId" : request.getHeader("userId"));
        logger.setUserCode(StringUtils.isEmpty(request.getHeader("userCode")) ? "userCode" : request.getHeader("userCode"));
        logger.setFSysId(StringUtils.isEmpty(request.getHeader("fsysId")) ? "fsysId" : request.getHeader("fsysId"));
        logger.setUserName(StringUtils.isEmpty(request.getHeader("userName")) ? "userName" : URLDecoder.decode(request.getHeader("userName"), "utf-8"));
        logger.setUserHost(StringUtils.isEmpty(request.getHeader("userHost")) ? "userHost" : request.getHeader("userHost"));
        //保存刷新token信息
        logger.setRefreshtoken(StringUtils.isEmpty(request.getHeader(REFRESH_TOKEN))?REFRESH_TOKEN:request.getHeader(REFRESH_TOKEN));
    }

    public static String getHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        Enumeration headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map.toString();
    }

    public static String getParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        Enumeration parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }

        return map.toString();
    }

    public static String getArgs(JoinPoint joinPoint) {
        return Arrays.toString(joinPoint.getArgs());
    }

    public static void setRequest(HttpServletRequest request,LogEntityRequest entity){
        entity.setUrl(request.getRequestURI());
        entity.setStartTime(DateUtil.currentTime());
        entity.setClientIp(getCliectIp(request));
        entity.setMethod(request.getMethod());
        entity.setReqType(getRequestType(request));
        entity.setBrowser(WebUtil.getBrowserCode(request));
    }
}
