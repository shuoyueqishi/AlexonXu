package com.xlt.service.impl;

import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.utils.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.reflect.TypeToken;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.model.Model;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.ChatGptConfigVo;
import com.xlt.mapper.SystemConfigMapper;
import com.xlt.service.IOpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RefreshScope
public class OpenAIService implements IOpenAIService {

    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${chatgpt.token}")
    private String token;

    @Value("${chatgpt.openai-domain}")
    private String openAiDomain;

    @Value("${chatgpt.proxy-domain}")
    private String proxyDomain;

    @Value("${chatgpt.use-proxy}")
    private boolean useProxy;


    @Override
    public OpenAiResponse<Model> listModels() {
        log.info("list ChatGPT models");
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(null, token);
        ResponseEntity<String> responseEntity;
        if(useProxy) {
            responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/models", HttpMethod.GET, httpEntity, String.class);
        } else {
            responseEntity = remoteRestTemplate.exchange(openAiDomain + "/v1/models", HttpMethod.GET, httpEntity, String.class);
        }
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), "listModels call encounter error");
        Type type = new TypeToken<OpenAiResponse<Model>>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public CompletionResult createCompletion(CompletionRequest request) {
        log.info("create completion,params:{}", JSON.toJSONString(request));
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(request,token);
        ResponseEntity<String> responseEntity;
        if(useProxy) {
            responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/completions", HttpMethod.POST, httpEntity, String.class);
        } else {
            responseEntity = remoteRestTemplate.exchange(openAiDomain + "/v1/completions", HttpMethod.POST, httpEntity, String.class);
        }
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()),  "createCompletion call encounter error");
        Type type = new TypeToken<CompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        log.info("create chat completion,params:{}", JSON.toJSONString(request));
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(request,token);
        ResponseEntity<String> responseEntity;
        if(useProxy) {
            responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/chat/completions", HttpMethod.POST, httpEntity, String.class);
        } else {
            responseEntity = remoteRestTemplate.exchange(openAiDomain+ "/v1/chat/completions", HttpMethod.POST, httpEntity, String.class);
        }
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()),  "createChatCompletion call encounter error");
        Type type = new TypeToken<ChatCompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    private HttpEntity<Object> buildAuthedHttpEntity(Object request, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        Map<String, Object> requestMap = ObjectUtil.getNonNullFields(request);
        log.info("bodyMap={}", requestMap);
        return new HttpEntity<>(requestMap, headers);
    }

    private ChatGptConfigVo fetchChatGptConfig() {
        String configCode = "chat.gpt";
        Object chatGptConfigObj = redisTemplate.opsForValue().get(configCode);
        if (Objects.nonNull(chatGptConfigObj)) {
            return JSON.parseObject(chatGptConfigObj.toString(), ChatGptConfigVo.class);
        }
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_code", configCode);
        SystemConfigPo systemConfigPo = systemConfigMapper.selectOne(queryWrapper);
        AssertUtil.isNull(systemConfigPo, configCode + " configuration not exist in system");
        AssertUtil.isNull(systemConfigPo.getValue(), configCode + " config value can't be empty");
        ChatGptConfigVo chatGptConfigVo = JSON.parseObject(systemConfigPo.getValue().toString(), ChatGptConfigVo.class);
        redisTemplate.opsForValue().set(configCode, JSON.toJSONString(chatGptConfigVo), Duration.ofMinutes(1));
        return chatGptConfigVo;
    }
}
