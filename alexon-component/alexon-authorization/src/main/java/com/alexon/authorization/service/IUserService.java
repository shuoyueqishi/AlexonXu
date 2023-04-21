package com.alexon.authorization.service;


import com.alexon.authorization.model.vo.UpdatePwdUserVo;
import com.alexon.authorization.model.vo.UserVo;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IUserService extends IUserQueryService {
    /**
     * 用户登录
     *
     * @param userVo 用户信息
     * @return 返回值
     */
    DataResponse<Object> userLogin(UserVo userVo) throws NoSuchAlgorithmException;

    /**
     * 用户退出登录
     * @param userVo 用户信息
     * @return 返回值
     */
    BasicResponse userLogout(UserVo userVo);

    /**
     * 查询用户列表
     * @param userVo 查询条件
     * @return 查询结果
     */
    DataResponse<List<UserVo>> queryUserList(UserVo userVo);

    /**
     * 分页查询用户信息
     *
     * @param userVo 入参
     * @return 返回值
     */
    PagedResponse<List<UserVo>> queryUserPageList(UserVo userVo, PageVo pageVo);

    /**
     * add new user
     *
     * @param userVo user info
     * @return result
     */
    BasicResponse addUser(UserVo userVo);

    /**
     * 更新用户信息
     *
     * @param userVo 用户Vo
     * @return 返回值
     */
    BasicResponse updateUser(UserVo userVo);

    /**
     * 更新用户密码
     *
     * @param updatePwdUserVo 用户Vo
     * @return 返回值
     */
    BasicResponse updateUserPwd(@Valid @NotNull UpdatePwdUserVo updatePwdUserVo);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 返回值
     */
    BasicResponse deleteUserById(Long userId);
}
