package com.alexon.http;

import com.alexon.http.log.LogClientHttpRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(OkHttpClient.class)
@Slf4j
public class RemoteRestTemplateAutoConfiguration {

    @Autowired
    private OkHttpClient okHttpClient;

    @Bean("remoteRestTemplate")
    public RestTemplate getRestTemplate() {
        OkHttp3ClientHttpRequestFactory okHttpFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        RestTemplate restTemplate = new RestTemplate(okHttpFactory);

        // 设置日志拦截器
        List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new LogClientHttpRequestInterceptor());
        restTemplate.setInterceptors(interceptorList);

        // 解决中文乱码
        List<HttpMessageConverter<?>> msgConvertorList = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : msgConvertorList) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
                break;
            }
        }
        return restTemplate;
    }
}
