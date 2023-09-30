package com.xlt.service.impl;

import com.xlt.model.request.WenxinChatReq;
import com.xlt.model.request.WenxinMessage;
import com.xlt.model.response.WenxinAccessTokenRes;
import com.xlt.model.response.WenxinChatRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class WenxinServiceTest {

    @Autowired
    private WenxinService wenxinService;

    @Test
    void fetchAccessToken() {
        WenxinAccessTokenRes tokenRes = wenxinService.fetchAccessToken();
        log.info("tokenRes={}", tokenRes);
    }

    @Test
    void chatWithErnieBor() {
        WenxinChatReq req = new WenxinChatReq();
        WenxinMessage message = WenxinMessage.builder().role("user").content("你好，请简单介绍一下你自己").build();
        req.setMessages(Arrays.asList(message));
        WenxinChatRes wenxinChatRes = wenxinService.chatWithErnieBor(req);
        log.info("wenxinChatRes={}", wenxinChatRes);
    }
}
