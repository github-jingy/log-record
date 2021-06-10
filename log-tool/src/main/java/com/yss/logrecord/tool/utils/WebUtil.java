package com.yss.logrecord.tool.utils;

import com.yss.logrecord.tool.constant.HeaderConstant;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {



    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public static HttpServletResponse getResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        return response;
    }

    public static String getBrowserCode(HttpServletRequest request){
        String header = request.getHeader(HeaderConstant.USER_AGENT).toLowerCase();
        return matchCode(header);
    }

    public static String matchCode(String header){
        if(StringUtils.isEmpty(header)){
            return HeaderConstant.OTHER_CODE;
        }
        if(header.indexOf(HeaderConstant.IE_MARK) > 0 ||
                (header.indexOf(HeaderConstant.IE_H_MARK) > 0 && header.indexOf(HeaderConstant.IE_H1_MARK) > 0)){
            return HeaderConstant.IE_CODE;
        }
        if(header.indexOf(HeaderConstant.FIREFOX_CODE) > 0){
            return HeaderConstant.FIREFOX_CODE;
        }
        if(header.indexOf(HeaderConstant.CHROME_CODE) > 0){
            return HeaderConstant.CHROME_CODE;
        }
        if(header.indexOf(HeaderConstant.SAFARI_CODE) > 0 && header.indexOf(HeaderConstant.CHROME_CODE) > 0){
            return HeaderConstant.SAFARI_CODE;
        }
        return HeaderConstant.OTHER_CODE;
    }


}
