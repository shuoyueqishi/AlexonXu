package com.xlt.controller;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import com.xlt.config.http.AuthenticatorExt;
import com.xlt.model.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.time.Duration;

@RestController
@RequestMapping("/chatgpt")
@Slf4j
public class ChatGPTController {




    @RequestMapping(value = "/question", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<String> questionChatGPT(@RequestBody String prompt) {
        OpenAiService service = new OpenAiService("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", Duration.ofSeconds(20));
       log.info("\nCreating completion,prompt={}",prompt);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .prompt(prompt)
                .echo(true)
                .user("xulitao666@gmail.com")
                .n(3)
                .build();
        CompletionResult completionResult = service.createCompletion(completionRequest);
        StringBuilder resultSb = new StringBuilder();
        completionResult.getChoices().forEach(action->{
            resultSb.append(action.getText()).append("\n");
        });

        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("8.218.219.31", 19220)));
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        Authenticator.setDefault(new AuthenticatorExt("pe68v5qBZn","bOfSPLLeGK"));
        restTemplate.postForEntity()
        return new DataResponse<>(resultSb.toString());
    }
}
