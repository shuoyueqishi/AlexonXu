package com.alexon.authorization.service.impl;

import com.alexon.authorization.config.JwtConfig;
import com.alexon.authorization.constants.CommConstant;
import com.alexon.authorization.context.UserContext;
import com.alexon.authorization.enums.LoginEnum;
import com.alexon.authorization.model.convertor.UserConvertor;
import com.alexon.authorization.model.request.LoginReq;
import com.alexon.authorization.model.request.WechatLoginReq;
import com.alexon.authorization.model.response.WechatLoginRes;
import com.alexon.authorization.model.vo.*;
import com.alexon.authorization.service.IWechatService;
import com.alexon.authorization.service.impl.asyncTasks.UserAsyncTasks;
import com.alexon.authorization.utils.*;
import com.alexon.exception.CommonException;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.utils.ObjectUtil;
import com.alexon.model.vo.PageVo;
import com.alexon.limiter.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.alexon.authorization.mapper.IRoleMapper;

import com.alexon.authorization.mapper.IRolePermissionMapper;
import com.alexon.authorization.mapper.IUserMapper;

import com.alexon.authorization.model.po.RolePo;
import com.alexon.authorization.model.po.UserPo;

import com.alexon.authorization.service.IUserService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private IRoleMapper roleMapper;

    @Autowired
    private IRolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserAsyncTasks userAsyncTasks;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private IWechatService wechatService;

    /**
     * 根据用户userId 列表获取用户数据，调用该接口之后，用户信息会缓存在Redis中
     *
     * @param userIdSet userIdSet
     * @return 用户信息
     */
    @Override
    public Map<Long, UserVo> fetchUserInfo(Set<Long> userIdSet) {
        log.info("fetchUserInfo query params={}", userIdSet);
        Map<Long, UserVo> userMap = new HashMap<>();
        if (CollectionUtils.isEmpty(userIdSet)) {
            return userMap;
        }
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIdSet);
        List<UserPo> userPoList = userMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userPoList)) {
            return userMap;
        }
        List<UserVo> userVoList = ObjectConvertUtil.convertObjsList(userPoList, UserVo.class);
        userVoList.forEach(userVo -> {
            userVo.setPassword(null);
            RedisUtil.set(CommConstant.USER_NAMES_PREFIX + userVo.getUserId(), userVo, 12, TimeUnit.HOURS);
            userMap.put(userVo.getUserId(), userVo);
        });
        return userMap;
    }

    /**
     * 用户登录
     *
     * @param loginReq 登录请求
     * @return 返回值
     */
    @Override
    public DataResponse<UserInfoVo> userLogin(LoginReq loginReq) {
        log.info("current user info:{}", loginReq);
        DataResponse<UserInfoVo> response = new DataResponse<>();
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        UserVo curUser;
        UserPo userPo=new UserPo();
        UserInfoVo userInfoVo = new UserInfoVo();
        String loginType = loginReq.getLoginType();
        if (LoginEnum.WX_MINI_PROGRAM.getType().equals(loginType)) { // 微信小程序登录
            WechatLoginReq wechatLoginReq = WechatLoginReq.builder().js_code(loginReq.getJs_code()).build();
            WechatLoginRes wechatLoginRes = wechatService.fetchWxOpenId(wechatLoginReq);
            userInfoVo.setWechatLoginRes(wechatLoginRes);
            queryWrapper.eq("open_id", wechatLoginRes.getOpenid());
            userPo = userMapper.selectOne(queryWrapper);
            if(Objects.isNull(userPo)) {
                // 新启动事务新增用户
                UserVo userVo = UserVo.builder().openId(wechatLoginRes.getOpenid())
                        .unionId(wechatLoginRes.getUnionid()).build();
                this.addUser(userVo);
                userPo = userMapper.selectOne(queryWrapper);
                AssertUtil.isNull(userPo, "User [" + loginReq.getOpenId() + "] not exists in system.");
            }
            curUser = UserConvertor.INSTANCE.toUserVo(userPo);
            userInfoVo.setCurUser(curUser);
        } else if(LoginEnum.NORMAL.getType().equals(loginType)) {
            AssertUtil.isStringEmpty(loginReq.getName(), "username can't be null");
            AssertUtil.isStringEmpty(loginReq.getPassword(), "user password can't be null");
            queryWrapper.eq("name", loginReq.getName());
            userPo = userMapper.selectOne(queryWrapper);
            AssertUtil.isNull(userPo, "User [" + loginReq.getName() + "] not exists in system.");
            curUser = UserConvertor.INSTANCE.toUserVo(userPo);
            AssertUtil.isNotTrue(MD5Util.validPassword(loginReq.getPassword() + userPo.getSalt(), userPo.getPassword()), "user password is invalid.");
            // 查询用户的当前角色
            curUser.setPassword(null);
            curUser.setSalt(null);
            userInfoVo.setCurUser(curUser);
        }
        RolePo curRolePo = roleMapper.selectById(userPo.getDefaultRole());
        AssertUtil.isNull(curRolePo,"default role not exist in system,roleId="+userPo.getDefaultRole());
        RoleVo curRoleVo = ObjectConvertUtil.convertObjs(curRolePo, RoleVo.class);
        userInfoVo.setCurRole(curRoleVo);

        // 查询当前角色的权限列表
        List<PermissionVo> permissionVoList = rolePermissionMapper.queryPermissionByRoleId(userPo.getDefaultRole());
        Set<String> permissionSet = new HashSet<>();
        permissionVoList.forEach(perm -> {
            permissionSet.add(perm.getTenant() + "#" + perm.getResourceName() + "#" + perm.getOperateCode());
        });
        userInfoVo.setCurPermissionList(new ArrayList<>(permissionSet));

        // 查询用户的角色列表
        List<UserRoleVo> validRoleList = rolePermissionMapper.queryRoleListByUserId(userPo.getUserId());
        userInfoVo.setValidRoleList(validRoleList);

        // 生产token
        String token = JwtUtil.createToken(userInfoVo);
        userInfoVo.setToken(token);
        String userInfoKey = CommConstant.USER_INFO_PREFIX + userPo.getUserId();
        log.info("user info cache key={}", userInfoKey);
        Map<String, Object> userInfoMap = ObjectUtil.getNonNullFields(userInfoVo);
        RedisUtil.hmset(userInfoKey, userInfoMap, jwtConfig.getJwtExpireTime());
        UserContext.setUserInfo(userInfoVo);
        response.setData(userInfoVo);
        response.setMessage("Login successfully for user " + loginReq.getName());
        return response;
    }

    /**
     * 用户退出登录
     *
     * @param userVo 用户信息
     * @return 返回值
     */
    @Override
    public BasicResponse userLogout(UserVo userVo) {
        AssertUtil.isNull(userVo.getUserId(), "userId can't be null");
        String tokenKey = CommConstant.TOKEN_PREFIX + userVo.getUserId();
        String userInfoKey = CommConstant.USER_INFO_PREFIX + userVo.getUserId();
        RedisUtil.delete(tokenKey);
        RedisUtil.delete(userInfoKey);
        return new BasicResponse();
    }

    @Override
    public DataResponse<List<UserVo>> queryUserList(UserVo userVo) {
        log.info("query params:{}", userVo);
        List<UserPo> userPos = queryUserPos(userVo);
        List<UserVo> userVos = ObjectConvertUtil.convertObjsList(userPos, UserVo.class);
        VoUtil.fillUserNames(userVos);
        return new DataResponse<>(userVos);
    }

    private List<UserPo> queryUserPos(UserVo userVo) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(userVo)) {
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getName()), "name", userVo.getName());
            queryWrapper.eq(Objects.nonNull(userVo.getUserId()), "user_id", userVo.getUserId());
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getNickName()), "nick_name", userVo.getNickName());
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getTelephone()), "telephone", userVo.getTelephone());
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getEmail()), "email", userVo.getEmail());
        }
        queryWrapper.orderByDesc("last_update_date");
        return userMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询用户信息
     *
     * @param userVo 入参
     * @return 返回值
     */
    @Override
    public PagedResponse<List<UserVo>> queryUserPageList(UserVo userVo, PageVo pageVo) {
        PageHelper.startPage((int) pageVo.getCurrentPage(), (int) pageVo.getPageSize());
        List<UserPo> userPos = queryUserPos(userVo);
        PageInfo<UserPo> pageInfo = new PageInfo<>(userPos);
        PagedResponse<List<UserVo>> response = new PagedResponse<>();
        pageVo.setTotalPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        response.setPage(pageVo);
        List<UserVo> userVos = ObjectConvertUtil.convertObjsList(userPos, UserVo.class);
        VoUtil.fillUserNames(userVos);
        response.setData(userVos);
        return response;
    }

    /**
     * add new user
     *
     * @param userVo user info
     * @return result
     */
    @Override
    public BasicResponse addUser(UserVo userVo) {
        log.info("new user info:{}", userVo);
        AssertUtil.isNull(userVo, "userVo can't be null");
        QueryWrapper<RolePo> roleWrapper = new QueryWrapper<>();
        roleWrapper.eq("role_code", "Guest");
        RolePo defaultRolePo = roleMapper.selectOne(roleWrapper);
        AssertUtil.isNull(defaultRolePo, "Default role Guest not exist");
        UserPo userPo=new UserPo();
        if (StringUtils.isNotEmpty(userVo.getOpenId())) { // 添加微信用户
            QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("open_id", userVo.getOpenId());
            UserPo existUser = userMapper.selectOne(queryWrapper);
            AssertUtil.isTrue(Objects.nonNull(existUser), userVo.getOpenId() + " has existed in system.");
            userPo = UserConvertor.INSTANCE.toUserPo(userVo);
            userPo.setDefaultRole(defaultRolePo.getRoleId());
            userPo.setOpenId(userVo.getOpenId());
            userPo.setUnionId(userVo.getUnionId());
            PoUtil.buildCreateUserInfo(userPo);
            userMapper.addUser(userPo);
        } else { // 普通用户
            AssertUtil.isStringEmpty(userVo.getName(), "name can't be empty");
            AssertUtil.isStringEmpty(userVo.getPassword(), "password can't be empty");
            AssertUtil.isStringEmpty(userVo.getTelephone(), "telephone can't be empty");
            AssertUtil.isStringEmpty(userVo.getEmail(), "email can't be empty");
            QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", userVo.getName());
            UserPo existUser = userMapper.selectOne(queryWrapper);
            AssertUtil.isTrue(Objects.nonNull(existUser), userVo.getName() + " has existed in system.");
            userPo = UserConvertor.INSTANCE.toUserPo(userVo);
            String salt = RandomStringUtils.randomAlphanumeric(16);
            String encryptPassword = MD5Util.encryptPassword(userPo.getPassword() + salt);
            userPo.setPassword(encryptPassword);
            userPo.setSalt(salt);
            userPo.setDefaultRole(defaultRolePo.getRoleId());
            PoUtil.buildCreateUserInfo(userPo);
            userMapper.insert(userPo);
        }
        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setUserId(userPo.getUserId());
        userRoleVo.setUserName(userVo.getName());
        userRoleVo.setRoleId(defaultRolePo.getRoleId());
        userAsyncTasks.grantRole2UserTask(userRoleVo);
        log.info("grant role to user, async task is  UserAsyncTasks#grantRole2UserTask");
        return new BasicResponse("add user successfully.");
    }

    /**
     * 更新用户信息
     *
     * @param userVo 用户Vo
     * @return 返回值
     */
    @Override
    public BasicResponse updateUser(UserVo userVo) {
        log.info("update user info:{}", userVo);
        AssertUtil.isNull(userVo, "userVo can't be null");
        AssertUtil.isNull(userVo.getUserId(), "userId can't be empty");
        UserPo userPo = new UserPo();
        if (StringUtils.isNotEmpty(userVo.getName())) {
            userPo.setName(userVo.getName());
        }
        if (StringUtils.isNotEmpty(userVo.getNickName())) {
            userPo.setNickName(userVo.getNickName());
        }
        if (StringUtils.isNotEmpty(userVo.getEmail())) {
            userPo.setEmail(userVo.getEmail());
        }
        if (StringUtils.isNotEmpty(userVo.getTelephone())) {
            userPo.setTelephone(userVo.getTelephone());
        }
        if (StringUtils.isNotEmpty(userVo.getHeadImg())) {
            userPo.setHeadImg(userVo.getHeadImg());
        }
        if (Objects.nonNull(userVo.getDefaultRole())) {
            userPo.setDefaultRole(userVo.getDefaultRole());
        }
        if(StringUtils.isNotEmpty(userVo.getOpenId())) {
            userPo.setOpenId(userVo.getOpenId());
        }
        if(StringUtils.isNotEmpty(userVo.getUnionId())) {
            userPo.setUnionId(userVo.getUnionId());
        }
        userPo.setLastUpdateDate(new Date());
        userPo.setLastUpdateBy(UserContext.getUserId());
        UpdateWrapper<UserPo> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userVo.getUserId());
        userMapper.update(userPo, updateWrapper);
        return new BasicResponse();
    }

    /**
     * 更新用户密码
     *
     * @param pwdUserVo 用户Vo
     * @return 返回值
     */
    @Override
    public BasicResponse updateUserPwd(UpdatePwdUserVo pwdUserVo) {
        AssertUtil.isNull(pwdUserVo.getUserId(), "userId can't be empty");
        AssertUtil.isStringEmpty(pwdUserVo.getPassword(), "old password can't be empty");
        AssertUtil.isStringEmpty(pwdUserVo.getNewPassword(), "new password can't be empty");
        UserPo userPo = userMapper.selectById(pwdUserVo.getUserId());
        AssertUtil.isNull(userPo, "user not exist in system, userId=" + pwdUserVo.getUserId());
        // 校验输入的原始密码是否正确
        if (!MD5Util.validPassword(pwdUserVo.getPassword() + userPo.getSalt(), userPo.getPassword())) {
            throw new CommonException("Origin password is not correct.");
        }

        // 加密新密码
        String salt = RandomStringUtils.randomAlphanumeric(16);
        String encryptedPwd = MD5Util.encryptPassword(pwdUserVo.getNewPassword() + salt);
        UserPo updateUserPo = UserPo.builder()
                .userId(pwdUserVo.getUserId())
                .password(encryptedPwd)
                .salt(salt)
                .build();
        updateUserPo.setLastUpdateDate(new Date());
        updateUserPo.setLastUpdateBy(UserContext.getUserId());
        userMapper.updateById(updateUserPo);
        return new BasicResponse();
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 返回值
     */
    @Override
    public BasicResponse deleteUserById(Long userId) {
        log.info("delete userId:{}", userId);
        UserPo userPo = userMapper.selectById(userId);
        AssertUtil.isNull(userPo, "user not exists in system, delete failed.");
        userMapper.deleteById(userId);
        return new BasicResponse();
    }
}
