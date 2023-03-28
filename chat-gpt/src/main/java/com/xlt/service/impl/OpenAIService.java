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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;

@Service
@Slf4j
@NacosPropertySource(dataId = "chat-gpt.yml",autoRefreshed = true)
public class OpenAIService implements IOpenAIService {

    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @NacosValue(value = "${chatgpt.token}", autoRefreshed = true)
    private String token;

    @NacosValue(value = "${chatgpt.proxy.domain}", autoRefreshed = true)
    private String proxyDomain;


    @Override
    public OpenAiResponse<Model> listModels() {
        log.info("list ChatGPT models");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/models", HttpMethod.GET, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), proxyDomain + "/chatgpt/v1/models call encounter error");
        Type type = new TypeToken<OpenAiResponse<Model>>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public CompletionResult createCompletion(CompletionRequest request) {
        log.info("create completion,params:{}", JSON.toJSONString(request));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/completions", HttpMethod.POST, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), proxyDomain + "/chatgpt/v1/completions call encounter error");
        Type type = new TypeToken<CompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        log.info("create chat completion,params:{}", JSON.toJSONString(request));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/chat/completions", HttpMethod.POST, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), proxyDomain + "/chatgpt/v1/chat/completions call encounter error");
        Type type = new TypeToken<ChatCompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }
}
