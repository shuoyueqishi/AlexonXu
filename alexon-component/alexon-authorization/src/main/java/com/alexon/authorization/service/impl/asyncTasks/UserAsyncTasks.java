package com.alexon.authorization.service.impl.asyncTasks;


import com.alexon.authorization.model.vo.UserRoleVo;
import com.alexon.authorization.service.impl.PermissionService;
import com.alexon.authorization.utils.DateUtils;
import com.alexon.exception.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alexon.authorization.mapper.IUserMapper;
import com.alexon.authorization.model.po.UserPo;
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
        AssertUtil.isNull(userRoleVo.getUserId(),"userId can't be empty");
        AssertUtil.isNull(userRoleVo.getRoleId(),"roleId can't be empty");
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userRoleVo.getUserId());
        UserPo userPo = userMapper.selectOne(queryWrapper);
        AssertUtil.isNull(userPo, userRoleVo.getUserId()+" not exist in system");
        userRoleVo.setUserId(userPo.getUserId());
        Date startDate = new Date();
        Date endDate = DateUtils.addYear(startDate, 1);
        userRoleVo.setStartTime(startDate);
        userRoleVo.setEndTime(endDate);
        permissionService.grantRoles2User(Collections.singletonList(userRoleVo));
        log.info("end of grantRole2UserTask");
    }
}
