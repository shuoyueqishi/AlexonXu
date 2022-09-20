package com.xlt.service;

import com.xlt.model.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface IUserQueryService {
    /**
     * 根据用户userId 列表获取用户数据，调用该接口之后，用户信息会缓存在Redis中
     *
     * @param userIdList userIdList
     * @return 用户信息
     */
    Map<Long, UserVo> fetchUserInfo(List<Long> userIdList);
}
