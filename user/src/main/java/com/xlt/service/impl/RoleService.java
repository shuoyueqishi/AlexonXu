package com.xlt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xlt.auth.JwtConfig;
import com.xlt.constant.CommConstant;
import com.xlt.context.UserContext;
import com.xlt.exception.CommonException;
import com.xlt.mapper.RoleMapper;
import com.xlt.mapper.RolePermissionMapper;
import com.xlt.mapper.UserMapper;
import com.xlt.model.po.RolePo;
import com.xlt.model.po.UserPo;
import com.xlt.model.response.DataResponse;
import com.xlt.model.response.PageDataResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.PermissionVo;
import com.xlt.model.vo.RoleVo;
import com.xlt.model.vo.UserInfoVo;
import com.xlt.service.api.IRoleService;
import com.xlt.utils.TkPoUtil;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.ObjectUtil;
import com.xlt.utils.common.RedisUtil;
import com.xlt.utils.common.VoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 新增用户
     *
     * @param roleVo 用户信息
     * @return 返回值
     */
    @Override
    public DataResponse<Object> roleAdd(RoleVo roleVo) {
        log.info("add new role params:{}",roleVo);
        Asserts.notNull(roleVo.getRoleCode(),"role code can't be empty.");
        Asserts.notNull(roleVo.getRoleName(),"role name can't be empty.");
        Asserts.notNull(roleVo.getTenant(),"tenant can't be empty.");
        QueryWrapper<RolePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code",roleVo.getRoleCode());
        RolePo existRolePo = roleMapper.selectOne(queryWrapper);
        if (Objects.nonNull(existRolePo))  {
            throw new CommonException(roleVo.getRoleCode()+" already exists in system.");
        }
        RolePo rolePo = RolePo.builder()
                .roleCode(roleVo.getRoleCode())
                .roleName(roleVo.getRoleName())
                .roleDesc(roleVo.getRoleDesc())
                .tenant(roleVo.getTenant())
                .build();
        TkPoUtil.buildCreateUserInfo(rolePo);
        roleMapper.insert(rolePo);
        return new DataResponse<>();
    }

    /**
     * 更新用户
     *
     * @param roleVo 用户信息
     * @return 返回值
     */
    @Override
    public DataResponse<Object> roleUpdate(RoleVo roleVo) {
        log.info("update user info");
        Asserts.notNull(roleVo.getRoleId(),"roleId can't be empty");
        RolePo updRolePo = new RolePo();
        updRolePo.setRoleId(roleVo.getRoleId());
        if(StringUtils.isNotEmpty(roleVo.getRoleCode())) {
            updRolePo.setRoleCode(roleVo.getRoleCode());
        }
        if (StringUtils.isNotEmpty(roleVo.getRoleName())) {
            updRolePo.setRoleName(roleVo.getRoleName());
        }
        if(StringUtils.isNotEmpty(roleVo.getRoleDesc())) {
            updRolePo.setRoleDesc(roleVo.getRoleDesc());
        }
        if (StringUtils.isNotEmpty(roleVo.getTenant())) {
            updRolePo.setTenant(roleVo.getTenant());
        }
        TkPoUtil.buildUpdateUserInfo(updRolePo);
        roleMapper.updateById(updRolePo);
        return DataResponse.builder().build();
    }

    /**
     * 分页查询用户信息
     *
     * @param roleVo 用户查询条件
     * @param pageVo 分页信息
     * @return 查询结果
     */
    @Override
    public PageDataResponse<RoleVo> queryRolePageList(RoleVo roleVo, PageVo pageVo) {
        log.info("queryRolePageList params:{}",roleVo);
        PageHelper.startPage((int)pageVo.getCurrentPage(), (int)pageVo.getPageSize());
        List<RolePo> rolePos = queryRolePos(roleVo);
        PageInfo<RolePo> pageInfo = new PageInfo<>(rolePos);
        PageDataResponse<RoleVo> response = new PageDataResponse<>();
        pageVo.setTotalPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        response.setPageVo(pageVo);
        List<RoleVo> roleVos = ObjectUtil.convertObjsList(rolePos, RoleVo.class);
        VoUtil.fillUserNames(roleVos);
        response.setData(roleVos);
        return response;
    }

    private List<RolePo> queryRolePos(RoleVo roleVo) {
        QueryWrapper<RolePo> queryWrapper = new QueryWrapper<>();
        if (!Objects.isNull(roleVo)) {
            queryWrapper.eq(Objects.nonNull(roleVo.getRoleId()),"role_id",roleVo.getRoleId());
            queryWrapper.like(StringUtils.isNotEmpty(roleVo.getRoleCode()),"role_code",roleVo.getRoleCode());
            queryWrapper.like(StringUtils.isNotEmpty(roleVo.getRoleName()),"role_name",roleVo.getRoleName());
            queryWrapper.like(StringUtils.isNotEmpty(roleVo.getRoleDesc()),"role_desc",roleVo.getRoleDesc());
            queryWrapper.eq(StringUtils.isNotEmpty(roleVo.getTenant()),"tenant",roleVo.getTenant());
        }
        queryWrapper.orderByDesc("last_update_date");
        return roleMapper.selectList(queryWrapper);
    }

    /**
     * 查询角色列表
     *
     * @param roleVo 查询参数
     * @return 查询结果
     */
    @Override
    public DataResponse<Object> queryRoleList(RoleVo roleVo) {
        List<RolePo> rolePos = queryRolePos(roleVo);
        List<RoleVo> roleVos = ObjectUtil.convertObjsList(rolePos, RoleVo.class);
        VoUtil.fillUserNames(roleVos);
        return DataResponse.builder().data(roleVos).build();
    }

    /**
     * 删除用户
     *
     * @param roleId 用户id
     * @return 返回值
     */
    @Override
    public DataResponse<Object> deleteRoleById(Long roleId) {
        log.info("delete roleId={}",roleId);
        Asserts.notNull(roleId,"roleId can't be null.");
        roleMapper.deleteById(roleId);
        return new DataResponse<>();
    }

    /**
     * 改变角色
     *
     * @param roleCode 角色编码
     * @return 返回角色切换结果
     */
    @Override
    public DataResponse<Object> changeRole(String roleCode, String userId) {
        if (StringUtils.isEmpty(roleCode)) {
            throw new CommonException("new roleCode can't be empty");
        }
        if (StringUtils.isEmpty(userId)) {
            throw new CommonException("userId can't be empty");
        }

        Map<Object, Object> userInfoMap = RedisUtil.hmget(CommConstant.USER_INFO_PREFIX + userId);
        if (Objects.isNull(userInfoMap)) {
            throw new CommonException("user info cached not exist");
        }
        UserInfoVo userInfo = new UserInfoVo();
        ObjectUtil.convertMap2UserInfoVo(userInfoMap, userInfo);
        UserContext.setUserInfo(userInfo);
        UserPo userPo = userMapper.selectById(userId);
        if (Objects.isNull(userPo)) {
            throw new CommonException("User not exist in system, change role failed.");
        }

        List<String> roleList = userInfo.getValidRoleList().stream().map(RoleVo::getRoleCode).collect(Collectors.toList());
        if (roleCode.equals(userInfo.getCurRole().getRoleCode())) {
            log.info("no need to change role, roleCode={}",roleCode);
            return new DataResponse<>();
        }
        AssertUtil.isTrue(!roleList.contains(roleCode),"lack of role: "+roleCode);
        // 更新角色
        QueryWrapper<RolePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code",roleCode);
        RolePo rolePo = roleMapper.selectOne(queryWrapper);
        RoleVo changedRole = ObjectUtil.convertObjs(rolePo, RoleVo.class);
        userInfo.setCurRole(changedRole);

        // TODO: 设置用户的默认角色

        // 更新权限点
        // 查询当前角色的权限列表
        List<PermissionVo> permissionVoList = rolePermMapper.queryPermissionByRoleId(rolePo.getRoleId());
        Set<String> permissionSet = new HashSet<>();
        permissionVoList.forEach(perm->{
            permissionSet.add(perm.getTenant()+"#"+perm.getResourceName()+"#"+perm.getOperateCode());
        });
        userInfo.setCurPermissionList(new ArrayList<>(permissionSet));

        // 设置默认角色
        UserPo updUserPo = UserPo.builder()
                .userId(userInfo.getCurUser().getUserId())
                .defaultRole(rolePo.getRoleId())
                .build();
        TkPoUtil.buildUpdateUserInfo(updUserPo);
        userMapper.updateById(updUserPo);

        // 更新缓存
        Map<String, Object> newUserInfoMap = ObjectUtil.getNonNullFields(userInfo);
        RedisUtil.hmset(CommConstant.USER_INFO_PREFIX + userId, newUserInfoMap, jwtConfig.getJwtExpireTime());

        // 更新上下文
        UserContext.setUserInfo(userInfo);
        return new DataResponse<>(userInfo);
    }
}
