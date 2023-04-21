package com.xlt.service;

import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.xlt.model.vo.ReceiverInfoVo;

import java.util.List;

public interface IReceiverInfoService {
    BasicResponse createReceiverInfo(ReceiverInfoVo receiverInfoVo);

    PagedResponse<List<ReceiverInfoVo>> queryReceiverInfoPagedList(ReceiverInfoVo receiverInfoVo, PageVo pageVo);
}
