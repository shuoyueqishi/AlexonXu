package com.xlt.service;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.UserVo;

import java.util.List;

public interface IUserService {
    DataResponse<List<UserVo>> queryUserList(UserVo userVo);

    BasicResponse addUser(UserVo userVo);

    BasicResponse updateUser(UserVo userVo);

    BasicResponse deleteUser(Long userId);
}

