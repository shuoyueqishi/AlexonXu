package com.alexon.authorization.service.impl;

import com.alexon.authorization.config.JwtConfig;
import com.alexon.authorization.constants.CommConstant;
import com.alexon.authorization.context.UserContext;
import com.alexon.authorization.model.vo.PermissionVo;
import com.alexon.authorization.model.vo.RoleVo;
import com.alexon.authorization.model.vo.UserInfoVo;
import com.alexon.authorization.utils.ObjectConvertUtil;
import com.alexon.authorization.utils.PoUtil;
import com.alexon.utils.RedisUtil;
import com.alexon.authorization.utils.VoUtil;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.DataResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.utils.ObjectUtil;
import com.alexon.model.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.alexon.authorization.mapper.IRoleMapper;
import com.alexon.authorization.mapper.IRolePermissionMapper;
import com.alexon.authorization.mapper.IUserMapper;
import com.alexon.authorization.model.po.RolePo;
import com.alexon.authorization.model.po.UserPo;
import com.alexon.authorization.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService implements IRoleService {

    @Autowired
    private IRoleMapper IRoleMapper;

    @Autowired
    private IRolePermissionMapper rolePermMapper;

    @Autowired
    private IUserMapper IUserMapper;

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
        AssertUtil.isStringEmpty(roleVo.getRoleCode(),"role code can't be empty.");
        AssertUtil.isStringEmpty(roleVo.getRoleName(),"role name can't be empty.");
        AssertUtil.isStringEmpty(roleVo.getTenant(),"tenant can't be empty.");
        QueryWrapper<RolePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code",roleVo.getRoleCode());
        RolePo existRolePo = IRoleMapper.selectOne(queryWrapper);
        AssertUtil.isNonNull(existRolePo,roleVo.getRoleCode()+" already exists in system.");
        RolePo rolePo = RolePo.builder()
                .roleCode(roleVo.getRoleCode())
                .roleName(roleVo.getRoleName())
                .roleDesc(roleVo.getRoleDesc())
                .tenant(roleVo.getTenant())
                .build();
        PoUtil.buildCreateUserInfo(rolePo);
        IRoleMapper.insert(rolePo);
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
        AssertUtil.isNull(roleVo.getRoleId(),"roleId can't be empty");
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
        PoUtil.buildUpdateUserInfo(updRolePo);
        IRoleMapper.updateById(updRolePo);
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
    public PagedResponse<List<RoleVo>> queryRolePageList(RoleVo roleVo, PageVo pageVo) {
        log.info("queryRolePageList params:{}",roleVo);
        PageHelper.startPage((int)pageVo.getCurrentPage(), (int)pageVo.getPageSize());
        List<RolePo> rolePos = queryRolePos(roleVo);
        PageInfo<RolePo> pageInfo = new PageInfo<>(rolePos);
        PagedResponse<List<RoleVo>> response = new PagedResponse<>();
        pageVo.setTotalPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        response.setPage(pageVo);
        List<RoleVo> roleVos = ObjectConvertUtil.convertObjsList(rolePos, RoleVo.class);
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
        return IRoleMapper.selectList(queryWrapper);
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
        List<RoleVo> roleVos = ObjectConvertUtil.convertObjsList(rolePos, RoleVo.class);
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
        AssertUtil.isNull(roleId,"roleId can't be null.");
        IRoleMapper.deleteById(roleId);
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
        AssertUtil.isStringEmpty(roleCode, "roleCode can't be empty");
        AssertUtil.isStringEmpty(userId, "userId can't be empty");
        Map<Object, Object> userInfoMap = RedisUtil.hmget(CommConstant.USER_INFO_PREFIX + userId);
        AssertUtil.isNull(userInfoMap,"user info cached not exist");
        UserInfoVo userInfo = new UserInfoVo();
        ObjectConvertUtil.convertMap2UserInfoVo(userInfoMap, userInfo);
        UserContext.setUserInfo(userInfo);
        UserPo userPo = IUserMapper.selectById(userId);
        AssertUtil.isNull(userPo,"User not exist in system, change role failed.");
        List<String> roleList = userInfo.getValidRoleList().stream().map(RoleVo::getRoleCode).collect(Collectors.toList());
        if (roleCode.equals(userInfo.getCurRole().getRoleCode())) {
            log.info("no need to change role, roleCode={}",roleCode);
            return new DataResponse<>();
        }
        AssertUtil.isTrue(!roleList.contains(roleCode),"lack of role: "+roleCode);
        // 更新角色
        QueryWrapper<RolePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code",roleCode);
        RolePo rolePo = IRoleMapper.selectOne(queryWrapper);
        RoleVo changedRole = ObjectConvertUtil.convertObjs(rolePo, RoleVo.class);
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
        PoUtil.buildUpdateUserInfo(updUserPo);
        IUserMapper.updateById(updUserPo);

        // 更新缓存
        Map<String, Object> newUserInfoMap = ObjectUtil.getNonNullFields(userInfo);
        RedisUtil.hmset(CommConstant.USER_INFO_PREFIX + userId, newUserInfoMap, jwtConfig.getJwtExpireTime());

        // 更新上下文
        UserContext.setUserInfo(userInfo);
        return new DataResponse<>(userInfo);
    }
}
