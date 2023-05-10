package com.alexon.authorization.service;


import com.alexon.authorization.model.response.WechatLoginRes;
import com.alexon.authorization.model.request.WechatLoginReq;

public interface IWechatService {

    WechatLoginRes fetchWxOpenId(WechatLoginReq wechatLoginReq);
}
