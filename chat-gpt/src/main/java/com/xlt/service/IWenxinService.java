package com.xlt.service;

import com.xlt.model.request.WenxinChatReq;
import com.xlt.model.response.WenxinAccessTokenRes;
import com.xlt.model.response.WenxinChatRes;

public interface IWenxinService {

    WenxinAccessTokenRes fetchAccessToken();

    WenxinChatRes chatWithErnieBor(WenxinChatReq chatReq);
}
