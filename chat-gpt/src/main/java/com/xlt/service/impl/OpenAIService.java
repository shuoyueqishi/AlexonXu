package com.xlt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.shaded.com.google.common.reflect.TypeToken;
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
public class OpenAIService implements IOpenAIService {

    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @NacosValue(value = "${chatgpt.token}", autoRefreshed = true)
    private String token;


    @Override
    public OpenAiResponse<Model> listModels() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = remoteRestTemplate.exchange("https://alexon.website/chatgpt/v1/models", HttpMethod.GET, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()),"call https://alexon.website/chatgpt/v1/models error");
        Type type = new TypeToken<OpenAiResponse<Model>>(){}.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public CompletionResult createCompletion(CompletionRequest request) {
        return null;
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        return null;
    }
}
