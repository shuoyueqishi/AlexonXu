package com.xlt.service.impl.asyncTasks;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlt.mapper.IUserMapper;
import com.xlt.model.po.UserPo;
import com.xlt.model.vo.UserRoleVo;
import com.xlt.service.impl.PermissionService;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
@Slf4j
public class UserAsyncTasks {

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private PermissionService permissionService;

    @Async("asyncPoolTaskExecutor")
    public void grantRole2UserTask(UserRoleVo userRoleVo) {
        log.info("begin to do grantRole2UserTask, userRoleVo={}",userRoleVo);
        AssertUtil.isStringEmpty(userRoleVo.getUserName(),"userName can't be empty");
        AssertUtil.isNull(userRoleVo.getRoleId(),"roleId can't be empty");
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",userRoleVo.getUserName());
        UserPo userPo = userMapper.selectOne(queryWrapper);
        AssertUtil.isNull(userPo, userRoleVo.getUserName()+" not exist in system");
        userRoleVo.setUserId(userPo.getUserId());
        Date startDate = new Date();
        Date endDate = DateUtils.addYear(startDate, 1);
        userRoleVo.setStartTime(startDate);
        userRoleVo.setEndTime(endDate);
        permissionService.grantRoles2User(Collections.singletonList(userRoleVo));
        log.info("end of grantRole2UserTask");
    }
}
