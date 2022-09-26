package com.xlt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xlt.model.response.PageDataResponse;
import com.xlt.service.ISyncPermissionService;
import com.xlt.constant.CommConstant;
import com.xlt.exception.CommonException;
import com.xlt.mapper.*;
import com.xlt.model.vo.PermissionVo;
import com.xlt.model.po.*;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.*;
import com.xlt.service.api.IPermissionService;
import com.xlt.utils.TkPoUtil;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.PermissionSyncUtil;
import com.xlt.utils.common.ObjectUtil;
import com.xlt.utils.common.VoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionService implements IPermissionService, ISyncPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;


    /**
     * 同步权限点
     *
     * @return 返回值
     */
    @Override
    @Transactional
    public BasicResponse synchronizePermission() {
        List<PermissionVo> permissionVoList = PermissionSyncUtil.getOperationPermissions();
        addPermissions(permissionVoList);
        return new BasicResponse();
    }

    private void addPermissions(List<PermissionVo> permissionVoList) {
        if (CollectionUtils.isEmpty(permissionVoList)) {
            return;
        }
        List<PermissionPo> permissionPoList = new ArrayList<>();
        permissionVoList.forEach(permissionVo -> {
            PermissionPo permissionPo = PermissionPo.builder()
                    .resourceName(permissionVo.getResourceName())
                    .operateCode(permissionVo.getOperateCode())
                    .operateDesc(permissionVo.getOperateDesc())
                    .path(permissionVo.getPath())
                    .tenant(permissionVo.getTenant())
                    .httpMethod(permissionVo.getHttpMethod())
                    .methodName(permissionVo.getMethodName())
                    .build();
            TkPoUtil.buildCreateUserInfo(permissionPo);
            permissionPoList.add(permissionPo);
        });
        permissionMapper.batchInsert(permissionPoList);

        RolePo sysAdminPo = roleMapper.selectOne(RolePo.builder().roleCode("SystemAdmin").build());
        AssertUtil.isNull(sysAdminPo, "role code: SystemAdmin not exist in system");

        List<PermissionVo> ownedPermList = rolePermissionMapper.queryPermissionByRoleId(sysAdminPo.getRoleId());
        Set<String> ownedPermSet = new HashSet<>();
        ownedPermList.forEach(perm -> {
            ownedPermSet.add(perm.getTenant() + "#" + perm.getResourceName() + "#" + perm.getOperateCode());
        });
        List<String> notOwnedList = new ArrayList<>();
        for (PermissionVo perm : permissionVoList) {
            String permPoint = perm.getTenant() + "#" + perm.getResourceName() + "#" + perm.getOperateCode();
            if (!ownedPermSet.contains(permPoint)) {
                notOwnedList.add(permPoint);
            }
        }

        if (!CollectionUtils.isEmpty(notOwnedList)) {
            List<PermissionPo> permissionPos = permissionMapper.queryPermissionsByPoint(notOwnedList);
            List<Long> permIdList = permissionPos.stream().map(PermissionPo::getPermissionId).collect(Collectors.toList());
            RolePermissionVo rolePermissionVo = new RolePermissionVo();
            rolePermissionVo.setRoleId(sysAdminPo.getRoleId());
            rolePermissionVo.setPermissionList(permIdList);
            grantPermissions2Role(rolePermissionVo);
        }
        log.info("synchronize permission successfully.");
    }

    @Override
    public BasicResponse syncPermissionList(List<PermissionVo> permVoList) {
        addPermissions(permVoList);
        return new BasicResponse();
    }

    /**
     * 给用户授权角色
     *
     * @param userRoleVoList 授权信息
     * @return 返回值
     */
    @Override
    public BasicResponse grantRoles2User(List<UserRoleVo> userRoleVoList) {
        log.info("grant roles info:{}", userRoleVoList);
        if (CollectionUtils.isEmpty(userRoleVoList)) {
            return new BasicResponse();
        }
        // 校验用户是否存在
        Set<Long> userIds = userRoleVoList.stream().map(UserRoleVo::getUserId).collect(Collectors.toSet());
        Example userExample = new Example(UserPo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andIn("userId", userIds);
        List<UserPo> userPoList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userPoList) || userPoList.size() != userIds.size()) {
            throw new CommonException("User maybe not exist in system, so it can't grant roles.");
        }

        // 校验角色是否存在
        Set<Long> roleIds = userRoleVoList.stream().map(UserRoleVo::getRoleId).collect(Collectors.toSet());
        Example roleExample = new Example(RolePo.class);
        Example.Criteria roleCriteria = roleExample.createCriteria();
        roleCriteria.andIn("roleId", roleIds);
        List<RolePo> rolePoList = roleMapper.selectByExample(roleExample);
        if (CollectionUtils.isEmpty(rolePoList) || rolePoList.size() != roleIds.size()) {
            throw new CommonException("Role may not exist in system, so it can't grant roles.");
        }

        // 校验权限有效时间
        for (UserRoleVo userRoleVo : userRoleVoList) {
            if (Objects.isNull(userRoleVo.getStartTime())) {
                throw new CommonException("startTime can't be empty.");
            }
            if (Objects.isNull(userRoleVo.getEndTime())) {
                throw new CommonException("endTome can't be empty");
            }
            if (userRoleVo.getEndTime().before(userRoleVo.getStartTime())) {
                throw new CommonException("endTime can't before startTime");
            }
        }

        // 授予角色
        List<UserRolePo> userRolePoList = ObjectUtil.convertObjsList(userRoleVoList, UserRolePo.class);
        userRolePoList.forEach(TkPoUtil::buildCreateUserInfo);
        userRoleMapper.batchInsert(userRolePoList);
        return new BasicResponse();
    }

    /**
     * 取消用户角色
     *
     * @param userRoleVoList 用户角色信息
     * @return 返回值
     */
    @Override
    public BasicResponse removeRole4User(List<UserRoleVo> userRoleVoList) {
        return null;
    }

    /**
     * 给角色授权权限
     *
     * @param rolePermissionVo 授权信息
     * @return 返回值
     */
    @Override
    public BasicResponse grantPermissions2Role(RolePermissionVo rolePermissionVo) {
        log.info("grantPermissions2Role input params:{}", rolePermissionVo);
        if (Objects.isNull(rolePermissionVo.getRoleId())) {
            throw new CommonException("roleId can't be empty");
        }
        if (CollectionUtils.isEmpty(rolePermissionVo.getPermissionList())) {
            throw new CommonException("permissionList can't be empty");
        }
        RolePo rolePo = roleMapper.selectByPrimaryKey(rolePermissionVo.getRoleId());
        if (Objects.isNull(rolePo)) {
            throw new CommonException("roleId=" + rolePermissionVo.getRoleId() + " not exists in system");
        }
        Example permExample = new Example(PermissionPo.class);
        Example.Criteria permCriteria = permExample.createCriteria();
        Set<Long> permSet = new HashSet<>(rolePermissionVo.getPermissionList());
        permCriteria.andIn("permissionId", permSet);
        List<PermissionPo> existPermPoList = permissionMapper.selectByExample(permExample);
        Set<Long> existPermSet = existPermPoList.stream().map(PermissionPo::getPermissionId).collect(Collectors.toSet());
        if (permSet.size() != existPermSet.size()) {
            throw new CommonException("permissionId not exist in system");
        }
        List<RolePermissionPo> rolePermList = new ArrayList<>();
        for (int i = 0; i < rolePermissionVo.getPermissionList().size(); i++) {
            RolePermissionPo rolePermissionPo = new RolePermissionPo();
            rolePermissionPo.setRoleId(rolePermissionVo.getRoleId());
            rolePermissionPo.setPermissionId(rolePermissionVo.getPermissionList().get(i));
            TkPoUtil.buildCreateUserInfo(rolePermissionPo);
            rolePermList.add(rolePermissionPo);
        }
        rolePermissionMapper.batchInsertPermissions(rolePermList);
        return new BasicResponse();
    }

    /**
     * 删除角色的角色
     *
     * @param rolePermissionVo 角色权限信息
     * @return 返回值
     */
    @Override
    public BasicResponse removeRolePermission(RolePermissionVo rolePermissionVo) {
        log.info("removeRolePermission input params:{}", rolePermissionVo);
        AssertUtil.isNull(rolePermissionVo, "rolePermissionVo is null");
        AssertUtil.isNull(rolePermissionVo.getRoleId(), "roleId can't be empty");
        AssertUtil.isCollectionEmpty(rolePermissionVo.getPermissionList(), "permissionList can't be empty");
        AssertUtil.isTrue(rolePermissionVo.getPermissionList().size() > 500, "PermissionList size can be larger than 500");
        rolePermissionMapper.removeRolePermission(rolePermissionVo);
        return new BasicResponse("remove role permissions successfully");
    }

    /**
     * 查询权限列表
     *
     * @param permissionVo 查询参数
     * @param pageVo       分页参数
     * @return 返回值
     */
    @Override
    public PageDataResponse<PermissionVo> queryPermissionPageList(PermissionVo permissionVo, PageVo pageVo) {
        log.info("queryPermissionPageList input params:{}", permissionVo);
        PageHelper.startPage((int) pageVo.getCurrentPage(), (int) pageVo.getPageSize());
        List<PermissionPo> permissionPoList = fetchPermissionPos(permissionVo);
        PageInfo<PermissionPo> pageInfo = new PageInfo<>(permissionPoList);
        PageDataResponse<PermissionVo> response = new PageDataResponse<>();
        pageVo.setTotalPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        response.setPageVo(pageVo);
        List<PermissionVo> permissionVos = ObjectUtil.convertObjsList(permissionPoList, PermissionVo.class);
        VoUtil.fillUserNames(permissionVos);
        response.setData(permissionVos);
        return response;
    }

    List<PermissionPo> fetchPermissionPos(PermissionVo permissionVo) {
        List<PermissionPo> permissionPoList = new ArrayList<>();
        if (Objects.nonNull(permissionVo)) {
            Example example = new Example(PermissionPo.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(permissionVo.getOperateDesc())) {
                criteria.andLike("operateDesc", CommConstant.PERCENTAGE + permissionVo.getOperateDesc() + CommConstant.PERCENTAGE);
            }
            if (StringUtils.isNotEmpty(permissionVo.getOperateCode())) {
                criteria.andLike("operateCode", permissionVo.getOperateCode() + CommConstant.PERCENTAGE);
            }
            if (StringUtils.isNotEmpty(permissionVo.getResourceName())) {
                criteria.andLike("resourceName", permissionVo.getResourceName() + CommConstant.PERCENTAGE);
            }
            if (StringUtils.isNotEmpty(permissionVo.getPath())) {
                criteria.andLike("path", permissionVo.getPath() + CommConstant.PERCENTAGE);
            }
            if (StringUtils.isNotEmpty(permissionVo.getTenant())) {
                criteria.andEqualTo("tenant", permissionVo.getTenant());
            }
            permissionPoList = permissionMapper.selectByExample(example);
        }
        return permissionPoList;
    }

    /**
     * 查询角色权限
     *
     * @param roleId 角色ID
     * @return 权限点列表
     */
    @Override
    public DataResponse<Object> queryRolePermissionList(Long roleId) {
        log.info("queryRolePermissionList roleId={}", roleId);
        if (Objects.isNull(roleId)) {
            throw new CommonException("roleId can't be empty.");
        }
        List<PermissionVo> permissionVoList = rolePermissionMapper.queryPermissionByRoleId(roleId);
        VoUtil.fillUserNames(permissionVoList);
        return new DataResponse<>(permissionVoList);
    }

    /**
     * 根据用户ID查询用户角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public DataResponse<Object> queryUserRoleList(Long userId) {
        log.info("queryUserRoleList userId={}", userId);
        if (Objects.isNull(userId)) {
            throw new CommonException("userId can't be empty");
        }
        List<UserRoleVo> roleVoList = rolePermissionMapper.queryRoleListByUserId(userId);
        VoUtil.fillUserNames(roleVoList);
        return new DataResponse<>(roleVoList);
    }
}
