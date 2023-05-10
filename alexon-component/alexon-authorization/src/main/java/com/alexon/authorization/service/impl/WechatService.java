package com.alexon.authorization.service.impl;

import com.alexon.authorization.constants.WechatConstant;
import com.alexon.authorization.mapper.IUserMapper;
import com.alexon.authorization.model.po.UserPo;
import com.alexon.authorization.model.response.WechatLoginRes;
import com.alexon.authorization.model.vo.UserInfoVo;
import com.alexon.authorization.model.vo.UserVo;
import com.alexon.authorization.model.request.WechatLoginReq;
import com.alexon.authorization.service.IWechatService;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.http.utils.HttpUtil;
import com.alexon.model.response.DataResponse;
import com.alexon.model.utils.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public WechatLoginRes fetchWxOpenId(WechatLoginReq wechatLoginReq) {
        log.info("wechatLoginReq={}", wechatLoginReq);
        wechatLoginReq.setAppid(appid);
        wechatLoginReq.setSecret(secret);
        Map<String, Object> urlParamMap = ObjectUtil.getNonNullFields(wechatLoginReq);
        String paramPathUrl = HttpUtil.buildPathUrl(wechatLoginReq);
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
