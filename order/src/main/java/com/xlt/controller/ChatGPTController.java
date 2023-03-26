package com.xlt.controller;

import com.xlt.model.response.DataResponse;
import com.xlt.utils.common.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/chatgpt")
@Slf4j
public class ChatGPTController {

    @RequestMapping(value = "/list/models", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<Object> listModels() {
        // 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

//        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
//        httpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("8.218.219.31", 19220)));
//        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
//        Authenticator.setDefault(new AuthenticatorExt("pe68v5qBZn","bOfSPLLeGK"));
        // internal用于内部鉴权
        HttpHeaders headers = new HttpHeaders();
        String token = "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";
        headers.add("Authorization","Bearer " + token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate =  new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://alexon.website/chatgpt/v1/models",HttpMethod.GET, httpEntity, String.class);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(responseEntity.getStatusCode()),"call http://alexon.website/chatgpt/v1/models error");
        log.info(responseEntity.getBody());
        return new DataResponse<>(responseEntity.getBody());
    }
}
