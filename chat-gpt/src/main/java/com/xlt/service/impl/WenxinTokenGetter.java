package com.xlt.service.impl;


import com.xlt.constants.WenxinConstant;
import com.xlt.model.response.WenxinAccessTokenRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WenxinTokenGetter {

    @Value("${wenxin.grant-type}")
    private String grantType;

    @Value("${wenxin.client-id}")
    private String clientId;

    @Value("${wenxin.client-secret}")
    private String clientSecret;


    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @Cacheable(value = "Wenxin")
    public WenxinAccessTokenRes fetchAccessToken(){
        String accessTokenUrl = buildAccessTokenUrl();
        ResponseEntity<WenxinAccessTokenRes> forEntity = remoteRestTemplate.getForEntity(accessTokenUrl, WenxinAccessTokenRes.class);
        return forEntity.getBody();
    }

    private String buildAccessTokenUrl() {
        StringBuilder url = new StringBuilder();
        url.append(WenxinConstant.URL.ACCESS_TOKEN)
                .append("?")
                .append("grant_type=").append(grantType)
                .append("&client_id=").append(clientId)
                .append("&client_secret=").append(clientSecret);
        return url.toString();
    }
}
