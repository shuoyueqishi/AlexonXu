package com.xlt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import com.xlt.utils.common.PermissionSyncUtil;
import com.xlt.utils.common.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionService implements IPermissionService {

    @Value("${spring.application.name}")
    private String tenant;

    @Autowired
    private PermissionSyncUtil permissionSyncUtil;

    @Autowired(required = false)
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
    public BasicResponse synchronizePermission() {
        List<PermissionVo> perAnnotationVoList = PermissionSyncUtil.getOperationPermissions();
        List<PermissionPo> permissionPoList = new ArrayList<>();
        perAnnotationVoList.forEach(permissionVo -> {
            PermissionPo permissionPo = PermissionPo.builder()
                    .apiOperation(permissionVo.getApiOperation())
                    .path(permissionVo.getPath())
                    .tenant(permissionVo.getTenant())
                    .httpMethod(permissionVo.getHttpMethod())
                    .methodName(permissionVo.getMethodName())
                    .build();
            TkPoUtil.buildCreateUserInfo(permissionPo);
            permissionPoList.add(permissionPo);
        });
        permissionMapper.batchInsert(permissionPoList);
        log.info("synchronize permission successfully.");
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
     * 查询权限列表
     *
     * @param permissionVo 查询参数
     * @param pageVo       分页参数
     * @return 返回值
     */
    @Override
    public DataResponse<Object> queryPermissionPageList(PermissionVo permissionVo, PageVo pageVo) {
        log.info("queryPermissionPageList input params:{}", permissionVo);
        PageHelper.startPage((int) pageVo.getCurrentPage(), (int) pageVo.getPageSize());
        List<PermissionPo> rolePos = fetchPermissionPos(permissionVo);
        PageInfo<PermissionPo> pageInfo = new PageInfo<>(rolePos);
        return DataResponse.builder().data(pageInfo).build();
    }

    List<PermissionPo> fetchPermissionPos(PermissionVo permissionVo) {
        List<PermissionPo> permissionPoList = new ArrayList<>();
        if (Objects.nonNull(permissionVo)) {
            Example example = new Example(PermissionPo.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(permissionVo.getApiOperation())) {
                criteria.andLike("apiOperation", permissionVo.getApiOperation() + CommConstant.PERCENTAGE);
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
        return new DataResponse<>(roleVoList);
    }
}
