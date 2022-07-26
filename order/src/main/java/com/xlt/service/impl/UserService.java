package com.xlt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlt.mapper.IUserMapper;
import com.xlt.model.po.UserPo;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.UserVo;
import com.xlt.service.interfaces.IUserService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public DataResponse<List<UserVo>> queryUserList(UserVo userVo) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(userVo.getId())) {
            queryWrapper.eq("id", userVo.getId());
        }
        if (StringUtils.isNotEmpty(userVo.getName())) {
            queryWrapper.like("name", userVo.getName()); // 全模糊
        }
        if (StringUtils.isNotEmpty(userVo.getNumber())) {
            queryWrapper.likeRight("number", userVo.getNumber()); // 后模糊
        }
        if (StringUtils.isNotEmpty(userVo.getPosition())) {
            queryWrapper.likeLeft("position", userVo.getPosition()); // 前模糊
        }
        List<UserPo> userPos = userMapper.selectList(queryWrapper);
        List<UserVo> userVos = new ArrayList<>();
        try {
            for (UserPo userPo:userPos) {
                UserVo tmp = new UserVo();
                BeanUtils.copyProperties(tmp, userPo);
                userVos.add(tmp);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("convert error:", e);
        }
        return new DataResponse<>(userVos);
    }

    @Override
    public BasicResponse addUser(UserVo userVo) {
        UserPo userPo = new UserPo();
        try {
            BeanUtils.copyProperties(userPo,userVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("copyProperties error:",e);
        }
        userMapper.insert(userPo);
        return new BasicResponse();
    }

    @Override
    public BasicResponse updateUser(UserVo userVo) {
        UserPo userPo = new UserPo();
        try {
            BeanUtils.copyProperties(userPo,userVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("copyProperties error:",e);
        }
        userMapper.updateById(userPo);
        return new BasicResponse();
    }

    @Override
    public BasicResponse deleteUser(Long userId) {
        userMapper.deleteById(userId);
        return new BasicResponse();
    }
}

