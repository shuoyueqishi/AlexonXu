package com.xlt.service;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.ReceiverInfoVo;

import java.util.List;

public interface IReceiverInfoService {
    BasicResponse createReceiverInfo(ReceiverInfoVo receiverInfoVo);

    PagedResponse<List<ReceiverInfoVo>>  queryReceiverInfoPagedList(ReceiverInfoVo receiverInfoVo, PageVo pageVo);
}
