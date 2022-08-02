package com.xlt;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.PermissionVo;

import java.util.List;

public interface ISyncPermissionService {
    BasicResponse syncPermissionList(List<PermissionVo> permVoList);
}
