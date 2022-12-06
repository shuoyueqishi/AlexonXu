package com.xlt.utils;

import com.xlt.context.UserContext;
import com.xlt.model.po.BasePo;
import com.xlt.model.po.TkBasePo;

import java.util.Date;

public class TkPoUtil {
    public static <T extends BasePo> void buildCreateUserInfo(T po) {
        po.setCreateBy(UserContext.getUserId());
        po.setLastUpdateBy(UserContext.getUserId());
        po.setCreationDate(new Date());
        po.setLastUpdateDate(new Date());
    }

    public static <T extends BasePo> void buildUpdateUserInfo(T po) {
        po.setLastUpdateBy(UserContext.getUserId());
        po.setLastUpdateDate(new Date());
    }
}
