package com.xlt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.shaded.com.google.common.reflect.TypeToken;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.model.Model;
import com.xlt.service.IOpenAIService;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Map;

@Service
@Slf4j
@RefreshScope
public class OpenAIService implements IOpenAIService {

    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @Value("${chatgpt.token}")
    private String token;

    @Value("${chatgpt.proxy.domain}")
    private String proxyDomain;


    @Override
    public OpenAiResponse<Model> listModels() {
        log.info("list ChatGPT models");
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(null);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/models", HttpMethod.GET, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), proxyDomain + "/chatgpt/v1/models call encounter error");
        Type type = new TypeToken<OpenAiResponse<Model>>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public CompletionResult createCompletion(CompletionRequest request) {
        log.info("create completion,params:{}", JSON.toJSONString(request));
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(request);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/completions", HttpMethod.POST, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), proxyDomain + "/chatgpt/v1/completions call encounter error");
        Type type = new TypeToken<CompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        log.info("create chat completion,params:{}", JSON.toJSONString(request));
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(request);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/chat/completions", HttpMethod.POST, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), proxyDomain + "/chatgpt/v1/chat/completions call encounter error");
        Type type = new TypeToken<ChatCompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    private HttpEntity<Object> buildAuthedHttpEntity(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        Map<String, Object> requestMap = ObjectUtil.getNonNullFields(request);
        log.info("bodyMap={}",requestMap);
        return new HttpEntity<>(requestMap, headers);
    }
}
