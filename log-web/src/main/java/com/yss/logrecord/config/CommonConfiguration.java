package com.yss.logrecord.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

@Component
@Configuration
public class CommonConfiguration {

    /** http请求socket连接超时时间,毫秒为单位 */

    public static final int HTTP_SOCKET_TIMEOUT = 2000;

    /** http请求连接超时时间,毫秒为单位 */

    public static final int HTTP_CONNECT_TIMEOUT = 2000;

    @Bean
    public RestTemplate template(){
        return new RestTemplate(getHttpRequestFactory());
    }

    private ClientHttpRequestFactory getHttpRequestFactory(){
        RequestConfig config = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(HTTP_CONNECT_TIMEOUT).build();
        CloseableHttpClient build = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        return new HttpComponentsClientHttpRequestFactory(build);
    }

    @Bean
    public ThreadPoolExecutor getMemoryPool(){
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20,
                50,40, TimeUnit.SECONDS,new LinkedBlockingQueue<>(2000));
        return poolExecutor;
    }
}
