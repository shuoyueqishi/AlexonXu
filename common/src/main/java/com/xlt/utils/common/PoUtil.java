package com.xlt.utils.common;

import com.xlt.context.UserContext;
import com.xlt.model.po.BasePo;

import java.util.Date;

public class PoUtil {
    public static <T extends BasePo> void buildCreateUserInfo(T po) {
        UserContext userContext = UserContext.getInstance();
        po.setCreateBy(userContext.getUserName());
        po.setLastUpdateBy(userContext.getUserName());
        po.setCreationDate(new Date());
        po.setLastUpdateDate(new Date());
    }

    public static <T extends BasePo> void buildUpdateUserInfo(T po) {
        UserContext userContext = UserContext.getInstance();
        po.setLastUpdateBy(userContext.getUserName());
        po.setLastUpdateDate(new Date());
    }
}
