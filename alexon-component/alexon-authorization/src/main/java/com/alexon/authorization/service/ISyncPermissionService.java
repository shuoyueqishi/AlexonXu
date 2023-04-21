package com.alexon.authorization.service;

import com.alexon.authorization.model.vo.PermissionVo;
import com.alexon.model.response.BasicResponse;

import java.util.List;

public interface ISyncPermissionService {
    BasicResponse syncPermissionList(List<PermissionVo> permVoList);
}
