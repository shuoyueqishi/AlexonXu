package com.xlt.service.impl;

import com.alexon.authorization.context.UserContext;
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
import com.xlt.mapper.ApiKeyMapper;
import com.xlt.model.po.OpenAiApiKeyPo;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.request.ApiKeyRequest;
import com.xlt.model.vo.AccessInfoVo;
import com.xlt.model.vo.ChatGptConfigVo;
import com.xlt.mapper.SystemConfigMapper;
import com.xlt.service.IOpenAIService;
import io.swagger.models.auth.In;
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
import java.util.concurrent.TimeUnit;

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
    private ApiKeyMapper apiKeyMapper;

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

    @Value("${chatgpt.daily-credit}")
    private Integer credit;

    private static final String API_KEY_PREFIX = "ApiKey:";

    private static final String CREDIT_PREFIX = "credit:";

    @Override
    public AccessInfoVo queryAccessInfo(Long userId) {
        log.info("userId={}",userId);
        if(Objects.isNull(userId)) {
            userId = UserContext.getUserId();
        }
        String apiKey = "";
        Object cachedKeyObj = redisTemplate.opsForValue().get(API_KEY_PREFIX + UserContext.getUserId());
        log.info("cachedKeyObj={}", cachedKeyObj);
        if (Objects.nonNull(cachedKeyObj)) {
            apiKey = cachedKeyObj.toString();
        } else {
            QueryWrapper<OpenAiApiKeyPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",userId);
            queryWrapper.orderByDesc("last_update_date");
            OpenAiApiKeyPo apiKeyPo = apiKeyMapper.selectOne(queryWrapper);
            if(Objects.nonNull(apiKeyPo)) {
                apiKey=apiKeyPo.getApiKey();
                redisTemplate.opsForValue().set(API_KEY_PREFIX+UserContext.getUserId(),apiKey);
            }
        }

        Integer creditCached  = (Integer)redisTemplate.opsForValue().get(CREDIT_PREFIX + UserContext.getUserId());
        Integer credit = this.credit;
        if(Objects.isNull(creditCached)) {
            redisTemplate.opsForValue().set(CREDIT_PREFIX+UserContext.getUserId(),this.credit,1,TimeUnit.DAYS);
        } else {
            credit = creditCached;
        }
        return AccessInfoVo.builder().credit(credit).apiKey(apiKey).userId(userId).build();
    }

    @Override
    public void saveOpenAiApiKey(ApiKeyRequest request) {
        AssertUtil.isStringEmpty(request.getApiKey(), "apiKey can't be empty");
        Long userId = request.getUserId();
        if (Objects.isNull(request.getUserId())) {
            userId = UserContext.getUserId();
        }
        log.info("userId={}", userId);
        OpenAiApiKeyPo po = OpenAiApiKeyPo.builder().userId(userId).apiKey(request.getApiKey()).build();
        apiKeyMapper.insert(po);
        redisTemplate.opsForValue().set(API_KEY_PREFIX + userId, request.getApiKey());
        log.info("save api key successfully.");
    }

    @Override
    public OpenAiResponse<Model> listModels() {
        log.info("list ChatGPT models");
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(null, token);
        ResponseEntity<String> responseEntity;
        if (useProxy) {
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
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(request, token);
        ResponseEntity<String> responseEntity;
        if (useProxy) {
            responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/completions", HttpMethod.POST, httpEntity, String.class);
        } else {
            responseEntity = remoteRestTemplate.exchange(openAiDomain + "/v1/completions", HttpMethod.POST, httpEntity, String.class);
        }
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), "createCompletion call encounter error");
        Type type = new TypeToken<CompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    @Override
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        log.info("create chat completion,params:{}", JSON.toJSONString(request));
        HttpEntity<Object> httpEntity = buildAuthedHttpEntity(request, token);
        ResponseEntity<String> responseEntity;
        if (useProxy) {
            responseEntity = remoteRestTemplate.exchange(proxyDomain + "/chatgpt/v1/chat/completions", HttpMethod.POST, httpEntity, String.class);
        } else {
            responseEntity = remoteRestTemplate.exchange(openAiDomain + "/v1/chat/completions", HttpMethod.POST, httpEntity, String.class);
        }
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()), "createChatCompletion call encounter error");
        Type type = new TypeToken<ChatCompletionResult>() {
        }.getType();
        return JSON.parseObject(responseEntity.getBody(), type);
    }

    private HttpEntity<Object> buildAuthedHttpEntity(Object request, String token) {
        HttpHeaders headers = new HttpHeaders();
        Object cachedKeyObj = redisTemplate.opsForValue().get(API_KEY_PREFIX + UserContext.getUserId());
        log.info("cachedKeyObj={}", cachedKeyObj);
        if (Objects.nonNull(cachedKeyObj)) {
            token = cachedKeyObj.toString();
        } else {
            log.info("user owner Key, check account credit");
            Integer creditCached = (Integer)redisTemplate.opsForValue().get(CREDIT_PREFIX + UserContext.getUserId());
            if(Objects.nonNull(creditCached)) {
                AssertUtil.isTrue(creditCached<=0,"你的今日积分已用完，请明日再试/在个人中心录入你的OpenAI API Key");
                redisTemplate.opsForValue().decrement(CREDIT_PREFIX+UserContext.getUserId(),1);
            } else {
                redisTemplate.opsForValue().set(CREDIT_PREFIX+UserContext.getUserId(), credit,1, TimeUnit.DAYS);
            }
        }
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
