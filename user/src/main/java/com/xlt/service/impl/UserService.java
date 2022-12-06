package com.xlt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xlt.constant.CommConstant;
import com.xlt.context.UserContext;
import com.xlt.exception.CommonException;
import com.xlt.exception.ErrorEnum;
import com.xlt.model.response.PageDataResponse;
import com.xlt.service.IUserQueryService;
import com.xlt.utils.common.*;
import com.xlt.utils.MD5Util;
import com.xlt.logs.OperationLog;
import com.xlt.mapper.RoleMapper;
import com.xlt.mapper.RolePermissionMapper;
import com.xlt.mapper.UserMapper;
import com.xlt.model.vo.PermissionVo;
import com.xlt.model.po.RolePo;
import com.xlt.model.po.UserPo;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.*;
import com.xlt.service.api.IUserService;
import com.xlt.utils.TkPoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

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
        List<UserPo> userPoList = userMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(userPoList)) {
            return userMap;
        }
        List<UserVo> userVoList = ObjectUtil.convertObjsList(userPoList, UserVo.class);
        userVoList.forEach(userVo -> {
            userVo.setPassword(null);
            RedisUtil.set(CommConstant.USER_NAMES_PREFIX+userVo.getUserId(), userVo,12, TimeUnit.HOURS);
            userMap.put(userVo.getUserId(),userVo);
        });
        return userMap;
    }

    @Override
    @OperationLog(operateModule = "UserService", operateType = "Login", operateDesc = "用户登录")
    public DataResponse<Object> userLogin(UserVo userVo) throws NoSuchAlgorithmException {
        log.info("current user info:{}", userVo);
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty(userVo.getName())) {
            throw new CommonException(ErrorEnum.PARAMETER_ERROR.getErrorCode(), "username can't be null");
        }
        if (StringUtils.isEmpty(userVo.getPassword())) {
            throw new CommonException(ErrorEnum.PARAMETER_ERROR.getErrorCode(), "user password can't be null");
        }
        queryWrapper.eq("name",userVo.getName());
        UserPo userPo = userMapper.selectOne(queryWrapper);
        DataResponse<Object> response = new DataResponse<>();
        if (Objects.isNull(userPo)) {
            throw new CommonException("User [" + userVo.getName() + "] not exists in system.");
        }
        UserInfoVo userInfoVo = new UserInfoVo();

        UserVo curUser = ObjectUtil.convertObjs(userPo, UserVo.class);
        userInfoVo.setCurUser(curUser);
        if (MD5Util.validPassword(userVo.getPassword(), userPo.getPassword())) {
            // 查询用户的当前角色
            curUser.setPassword(null);
            RolePo curRolePo = roleMapper.selectById(userPo.getDefaultRole());
            RoleVo curRoleVo = ObjectUtil.convertObjs(curRolePo, RoleVo.class);
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
            RedisUtil.hmset(userInfoKey, userInfoMap, 1800);
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
        Asserts.notNull(userVo.getUserId(), "userId can't be null");
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
        List<UserVo> userVos = ObjectUtil.convertObjsList(userPos, UserVo.class);
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
        return userMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询用户信息
     *
     * @param userVo 入参
     * @return 返回值
     */
    @Override
    public PageDataResponse<UserVo> queryUserPageList(UserVo userVo, PageVo pageVo) {
        PageHelper.startPage((int) pageVo.getCurrentPage(), (int) pageVo.getPageSize());
        List<UserPo> userPos = queryUserPos(userVo);
        PageInfo<UserPo> pageInfo = new PageInfo<>(userPos);
        PageDataResponse<UserVo> response = new PageDataResponse<>();
        pageVo.setTotalPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        response.setPageVo(pageVo);
        List<UserVo> userVos = ObjectUtil.convertObjsList(userPos, UserVo.class);
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
        Asserts.notNull(userVo, "userVo can't be null");
        Asserts.notNull(userVo.getName(), "name can't be empty");
        Asserts.notNull(userVo.getPassword(), "password can't be empty");
        Asserts.notNull(userVo.getTelephone(), "telephone can't be empty");
        Asserts.notNull(userVo.getEmail(), "email can't be empty");
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",userVo.getName());
        UserPo existUser = userMapper.selectOne(queryWrapper);
        if (Objects.nonNull(existUser)) {
            throw new CommonException(userVo.getName() + " has existed in system.");
        }
        UserPo userPo = ObjectUtil.convertObjs(userVo, UserPo.class);
        String encryptPassword = MD5Util.encryptPassword(userPo.getPassword());
        userPo.setPassword(encryptPassword);
        TkPoUtil.buildCreateUserInfo(userPo);
        userMapper.insert(userPo);
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
        Asserts.notNull(userVo, "userVo can't be null");
        Asserts.notNull(userVo.getUserId(), "userId can't be empty");
        UserPo userPo = ObjectUtil.convertObjs(userVo, UserPo.class);
        userPo.setLastUpdateDate(new Date());
        userPo.setLastUpdateBy(UserContext.getUserId());
        userMapper.updateById(userPo);
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
        Asserts.notNull(pwdUserVo.getUserId(), "userId can't be empty");
        Asserts.notNull(pwdUserVo.getPassword(), "old password can't be empty");
        Asserts.notNull(pwdUserVo.getNewPassword(), "new password can't be empty");
        UserPo userPo = userMapper.selectById(pwdUserVo.getUserId());
        Asserts.notNull(userPo, "user not exist in system, userId=" + pwdUserVo.getUserId());
        // 校验输入的原始密码是否正确
        try {
            if (!MD5Util.validPassword(pwdUserVo.getPassword(), userPo.getPassword())) {
                throw new CommonException("Origin password is not correct.");
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("valid password encounter error:", e);
        }
        // 加密新密码
        String encryptedPwd = MD5Util.encryptPassword(pwdUserVo.getNewPassword());
        UserPo updateUserPo = UserPo.builder()
                .userId(pwdUserVo.getUserId())
                .password(encryptedPwd)
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
        Asserts.notNull(userPo, "user not exists in system, delete failed.");
        userMapper.selectById(userId);
        return new BasicResponse();
    }

    public UserVo queryUserById(Long userId) {
        AssertUtil.isNull(userId,"userId can't be null.");
        UserPo userPo = userMapper.selectById(userId);
        return ObjectUtil.convertObjs(userPo, UserVo.class);
    }

}
