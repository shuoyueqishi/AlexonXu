package com.xlt.controller;


import com.alexon.model.response.DataResponse;
import com.xlt.model.response.WechatLoginRes;
import com.xlt.model.vo.WechatLoginVo;
import com.xlt.service.IWechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private IWechatService wechatService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    DataResponse<WechatLoginRes> login(@RequestBody WechatLoginVo wechatLoginVo) {
        WechatLoginRes loginRes = wechatService.login(wechatLoginVo);
        return new DataResponse<>(loginRes);
    }
}
