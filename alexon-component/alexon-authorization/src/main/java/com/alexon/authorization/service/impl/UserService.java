package com.alexon.authorization.service.impl;

import com.alexon.authorization.config.JwtConfig;
import com.alexon.authorization.constants.CommConstant;
import com.alexon.authorization.context.UserContext;
import com.alexon.authorization.model.vo.*;
import com.alexon.authorization.service.impl.asyncTasks.UserAsyncTasks;
import com.alexon.authorization.utils.*;
import com.alexon.exception.CommonException;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.utils.ObjectUtil;
import com.alexon.model.vo.PageVo;
import com.alexon.utils.RedisUtil;
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

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserMapper IUserMapper;

    @Autowired
    private IRoleMapper IRoleMapper;

    @Autowired
    private IRolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserAsyncTasks userAsyncTasks;

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 根据用户userId 列表获取用户数据，调用该接口之后，用户信息会缓存在Redis中
     *
     * @param userIdSet userIdSet
     * @return 用户信息
     */
    @Override
    public Map<Long, UserVo> fetchUserInfo(Set<Long> userIdSet) {
        log.info("fetchUserInfo query params={}",userIdSet);
        Map<Long,UserVo> userMap = new HashMap<>();
        if(CollectionUtils.isEmpty(userIdSet)) {
            return userMap;
        }
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id",userIdSet);
        List<UserPo> userPoList = IUserMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(userPoList)) {
            return userMap;
        }
        List<UserVo> userVoList = ObjectConvertUtil.convertObjsList(userPoList, UserVo.class);
        userVoList.forEach(userVo -> {
            userVo.setPassword(null);
            RedisUtil.set(CommConstant.USER_NAMES_PREFIX+userVo.getUserId(), userVo,12, TimeUnit.HOURS);
            userMap.put(userVo.getUserId(),userVo);
        });
        return userMap;
    }

    @Override
    public DataResponse<Object> userLogin(UserVo userVo) throws NoSuchAlgorithmException {
        log.info("current user info:{}", userVo);
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        AssertUtil.isStringEmpty(userVo.getName(),"username can't be null");
        AssertUtil.isStringEmpty(userVo.getPassword(),"user password can't be null");
        queryWrapper.eq("name",userVo.getName());
        UserPo userPo = IUserMapper.selectOne(queryWrapper);
        DataResponse<Object> response = new DataResponse<>();
        AssertUtil.isNull(userPo,"User [" + userVo.getName() + "] not exists in system.");
        UserInfoVo userInfoVo = new UserInfoVo();
        UserVo curUser = ObjectConvertUtil.convertObjs(userPo, UserVo.class);
        userInfoVo.setCurUser(curUser);
        if (MD5Util.validPassword(userVo.getPassword()+userPo.getSalt(), userPo.getPassword())) {
            // 查询用户的当前角色
            curUser.setPassword(null);
            curUser.setSalt(null);
            RolePo curRolePo = IRoleMapper.selectById(userPo.getDefaultRole());
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
            response.setMessage("Login successfully for user " + userVo.getName());
        } else {
            throw new CommonException("user password is invalid.");
        }

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
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getName()),"name",userVo.getName());
            queryWrapper.eq(Objects.nonNull(userVo.getUserId()),"user_id",userVo.getUserId());
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getNickName()),"nick_name",userVo.getNickName());
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getTelephone()),"telephone",userVo.getTelephone());
            queryWrapper.like(StringUtils.isNotEmpty(userVo.getEmail()),"email",userVo.getEmail());
        }
        queryWrapper.orderByDesc("last_update_date");
        return IUserMapper.selectList(queryWrapper);
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
        AssertUtil.isStringEmpty(userVo.getName(), "name can't be empty");
        AssertUtil.isStringEmpty(userVo.getPassword(), "password can't be empty");
        AssertUtil.isStringEmpty(userVo.getTelephone(), "telephone can't be empty");
        AssertUtil.isStringEmpty(userVo.getEmail(), "email can't be empty");
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",userVo.getName());
        UserPo existUser = IUserMapper.selectOne(queryWrapper);
        AssertUtil.isTrue(Objects.nonNull(existUser),userVo.getName() + " has existed in system.");
        QueryWrapper<RolePo> roleWrapper = new QueryWrapper<>();
        roleWrapper.eq("role_code","Guest");
        RolePo defaultRolePo = IRoleMapper.selectOne(roleWrapper);
        AssertUtil.isNull(defaultRolePo,"Default role Guest not exist");
        UserPo userPo = ObjectConvertUtil.convertObjs(userVo, UserPo.class);
        String salt = RandomStringUtils.randomAlphanumeric(16);
        String encryptPassword = MD5Util.encryptPassword(userPo.getPassword()+salt);
        userPo.setPassword(encryptPassword);
        userPo.setSalt(salt);
        userPo.setDefaultRole(defaultRolePo.getRoleId());
        PoUtil.buildCreateUserInfo(userPo);
        IUserMapper.insert(userPo);

        UserRoleVo userRoleVo = new UserRoleVo();
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
        if(StringUtils.isNotEmpty(userVo.getName())){
            userPo.setName(userVo.getName());
        }
        if(StringUtils.isNotEmpty(userVo.getNickName())) {
            userPo.setNickName(userVo.getNickName());
        }
        if(StringUtils.isNotEmpty(userVo.getEmail())) {
            userPo.setEmail(userVo.getEmail());
        }
        if(StringUtils.isNotEmpty(userVo.getTelephone())) {
            userPo.setTelephone(userVo.getTelephone());
        }
        if(StringUtils.isNotEmpty(userVo.getHeadImg())) {
            userPo.setHeadImg(userVo.getHeadImg());
        }
        if(Objects.nonNull(userVo.getDefaultRole())) {
            userPo.setDefaultRole(userVo.getDefaultRole());
        }
        userPo.setLastUpdateDate(new Date());
        userPo.setLastUpdateBy(UserContext.getUserId());
        UpdateWrapper<UserPo> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id",userVo.getUserId());
        IUserMapper.update(userPo,updateWrapper);
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
        UserPo userPo = IUserMapper.selectById(pwdUserVo.getUserId());
        AssertUtil.isNull(userPo, "user not exist in system, userId=" + pwdUserVo.getUserId());
        // 校验输入的原始密码是否正确
        try {
            if (!MD5Util.validPassword(pwdUserVo.getPassword()+userPo.getSalt(), userPo.getPassword())) {
                throw new CommonException("Origin password is not correct.");
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("valid password encounter error:", e);
        }
        // 加密新密码
        String salt = RandomStringUtils.randomAlphanumeric(16);
        String encryptedPwd = MD5Util.encryptPassword(pwdUserVo.getNewPassword()+salt);
        UserPo updateUserPo = UserPo.builder()
                .userId(pwdUserVo.getUserId())
                .password(encryptedPwd)
                .salt(salt)
                .build();
        updateUserPo.setLastUpdateDate(new Date());
        updateUserPo.setLastUpdateBy(UserContext.getUserId());
        IUserMapper.updateById(updateUserPo);
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
        UserPo userPo = IUserMapper.selectById(userId);
        AssertUtil.isNull(userPo, "user not exists in system, delete failed.");
        IUserMapper.deleteById(userId);
        return new BasicResponse();
    }
}