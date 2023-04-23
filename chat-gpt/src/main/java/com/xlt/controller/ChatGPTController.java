package com.xlt.controller;


import com.alexon.model.response.DataResponse;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.model.Model;
import com.xlt.service.IOpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Slf4j
public class ChatGPTController {

     @Autowired
     private IOpenAIService openAIService;

    @RequestMapping(value = "/v1/models", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<List<Model>> listModels() {
        OpenAiResponse<Model> openAiResponse = openAIService.listModels();
        return new DataResponse<>(openAiResponse.getData());
    }

    @RequestMapping(value = "/v1/chat/completions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatCompletionResult> chatCompletions(@RequestBody ChatCompletionRequest request) {
        ChatCompletionResult chatCompletion = openAIService.createChatCompletion(request);
        return new DataResponse<>(chatCompletion);
    }

    @RequestMapping(value = "/v1/completions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<CompletionResult> completions(@RequestBody CompletionRequest request) {
        CompletionResult completion = openAIService.createCompletion(request);
        return new DataResponse<>(completion);
    }


}
