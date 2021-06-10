package com.yss.logrecord.tool.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "logrecord.application")
public class FeignProperties {

    private static final String default_url = "http://log-record/";

    private String url;

    public String getUrl() {
        if(StringUtils.hasText(url)){
            return url;
        }
        return default_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
