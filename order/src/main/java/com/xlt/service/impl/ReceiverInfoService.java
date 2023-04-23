package com.xlt.service.impl;

import com.alexon.authorization.constants.RedisConstant;
import com.alexon.authorization.utils.ObjectConvertUtil;
import com.alexon.authorization.utils.PoUtil;
import com.alexon.limiter.utils.RedisUtil;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.mapper.IReceiverInfoMapper;
import com.xlt.model.po.ReceiverInfoPo;
import com.xlt.model.vo.ReceiverInfoVo;
import com.xlt.service.IReceiverInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReceiverInfoService implements IReceiverInfoService {

    @Autowired
    private IReceiverInfoMapper receiverInfoMapper;

    @Override
    public BasicResponse createReceiverInfo(ReceiverInfoVo receiverInfoVo) {
        log.info("create receiver info:{}",receiverInfoVo);
        AssertUtil.isNull(receiverInfoVo.getReceiverName(),"name can't be empty");
        AssertUtil.isStringEmpty(receiverInfoVo.getReceiverTelephone(),"telephone can't be empty");
        AssertUtil.isStringEmpty(receiverInfoVo.getReceiverAddress(),"address can't be empty");
        AssertUtil.isNull(receiverInfoVo.getUserId(),"userId can't be empty");
        ReceiverInfoPo receiverInfoPo = ObjectConvertUtil.convertObjs(receiverInfoVo, ReceiverInfoPo.class);
        PoUtil.buildCreateUserInfo(receiverInfoPo);
        receiverInfoMapper.insert(receiverInfoPo);
        RedisUtil.set(RedisConstant.RECEIVER_INFO + receiverInfoPo.getReceiverId(),receiverInfoPo);
        return new BasicResponse();
    }

    @Override
    public PagedResponse<List<ReceiverInfoVo>> queryReceiverInfoPagedList(ReceiverInfoVo receiverInfoVo, PageVo pageVo) {
        QueryWrapper<ReceiverInfoPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(receiverInfoVo.getReceiverId()!=null,"receiverId",receiverInfoVo.getReceiverId());
        queryWrapper.eq(receiverInfoVo.getUserId()!=null,"userId",receiverInfoVo.getUserId());
        queryWrapper.like(receiverInfoVo.getReceiverName()!=null,"receiverName",receiverInfoVo.getReceiverName());
        queryWrapper.eq(receiverInfoVo.getReceiverTelephone()!=null,"receiverTelephone",receiverInfoVo.getReceiverTelephone());
        Page<ReceiverInfoPo> page = new Page<>(pageVo.getCurrentPage(),pageVo.getPageSize());
        Page<ReceiverInfoPo> infoPoPage = receiverInfoMapper.selectPage(page, queryWrapper);
        List<ReceiverInfoVo> receiverInfoVos = ObjectConvertUtil.convertObjsList(infoPoPage.getRecords(), ReceiverInfoVo.class);
        pageVo.setTotalPages(page.getPages());
        pageVo.setTotal(page.getTotal());
        return new PagedResponse<>(receiverInfoVos, pageVo);
    }
}
