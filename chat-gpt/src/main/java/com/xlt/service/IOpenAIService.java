package com.xlt.service;

import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.model.Model;
import com.xlt.model.request.ApiKeyRequest;
import com.xlt.model.vo.AccessInfoVo;

/**
 * OpenAI 接口 ,参考 com.theokanning.openai.OpenAiApi
 * 参考：https://platform.openai.com/docs/api-reference/introduction
 */
public interface IOpenAIService {

    AccessInfoVo queryAccessInfo(Long userId);

    void saveOpenAiApiKey(ApiKeyRequest request);

    OpenAiResponse<Model> listModels();

    CompletionResult createCompletion(CompletionRequest request);

    ChatCompletionResult createChatCompletion(ChatCompletionRequest request);
}
