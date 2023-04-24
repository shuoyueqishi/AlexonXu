package com.xlt.service.impl;

import com.alexon.exception.utils.AssertUtil;
import com.alexon.http.utils.HttpUtil;
import com.alexon.model.utils.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.xlt.model.response.WechatLoginRes;
import com.xlt.model.vo.WechatLoginVo;
import com.xlt.constants.WechatConstant;
import com.xlt.service.IWechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class WechatService implements IWechatService {

    @Value("${wechat.miniprogram.appid}")
    private String appid;

    @Value("${wechat.miniprogram.secret}")
    private String secret;

    @Value("${wechat.miniprogram.base_url}")
    private String baseUrl;


    @Autowired
    @Qualifier("remoteRestTemplate")
    private RestTemplate remoteRestTemplate;

    @Override
    public WechatLoginRes login(WechatLoginVo wechatLoginVo) {
        log.info("wechatLoginVo={}", wechatLoginVo);
        wechatLoginVo.setAppid(appid);
        wechatLoginVo.setSecret(secret);
        Map<String, Object> urlParamMap = ObjectUtil.getNonNullFields(wechatLoginVo);
        String paramPathUrl = HttpUtil.buildPathUrl(wechatLoginVo);
        ResponseEntity<String> resEntity = remoteRestTemplate.getForEntity(baseUrl + WechatConstant.LOGIN_URL + paramPathUrl, String.class, urlParamMap);
        AssertUtil.isNotTrue(HttpStatus.OK.equals(resEntity.getStatusCode()), "login error:" + resEntity.getStatusCode());
        String bodyStr = resEntity.getBody();
        AssertUtil.isNull(bodyStr, "response body is null");
        WechatLoginRes wechatLoginRes = JSON.parseObject(bodyStr, WechatLoginRes.class);
        AssertUtil.isNull(wechatLoginRes,"empty response");
        AssertUtil.isNotTrue(Objects.isNull(wechatLoginRes.getErrcode())||WechatConstant.SUCCESS_CODE.equals(wechatLoginRes.getErrcode()), "login failed, because " + wechatLoginRes.getErrmsg());
        return wechatLoginRes;
    }



}
