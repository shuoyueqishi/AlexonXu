package com.xlt.service;

import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.model.Model;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * OpenAI 接口 ,参考 com.theokanning.openai.OpenAiApi
 * 参考：https://platform.openai.com/docs/api-reference/introduction
 */
public interface IOpenAIService {

    OpenAiResponse<Model> listModels();

    CompletionResult createCompletion(CompletionRequest request);

    ChatCompletionResult createChatCompletion(ChatCompletionRequest request);
}
