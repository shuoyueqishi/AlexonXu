package com.xlt.service;

import com.xlt.model.response.WechatLoginRes;
import com.xlt.model.vo.WechatLoginVo;

public interface IWechatService {

    WechatLoginRes login(WechatLoginVo wechatLoginVo);
}
