package com.xlt.service.impl;

import com.xlt.constants.WenxinConstant;
import com.xlt.model.request.WenxinChatReq;
import com.xlt.model.response.WenxinAccessTokenRes;
import com.xlt.model.response.WenxinChatRes;
import com.xlt.service.IWenxinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class WenxinService implements IWenxinService {

    @Autowired
    private WenxinTokenGetter tokenGetter;

    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @Override
    public WenxinAccessTokenRes fetchAccessToken() {
        return tokenGetter.fetchAccessToken();
    }


    @Override
    public WenxinChatRes chatWithErnieBor(WenxinChatReq chatReq) {
        WenxinAccessTokenRes tokenRes = tokenGetter.fetchAccessToken();
        String url = WenxinConstant.URL.ERNIE_BOR_CHAT + "?access_token=" + tokenRes.getAccess_token();
        ResponseEntity<WenxinChatRes> entity = remoteRestTemplate.postForEntity(url, chatReq, WenxinChatRes.class);
        return entity.getBody();
    }
}
