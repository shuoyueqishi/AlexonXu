package com.xlt.utils;

import com.xlt.context.UserContext;
import com.xlt.model.po.BasePo;
import com.xlt.model.po.TkBasePo;

import java.util.Date;

public class TkPoUtil {
    public static <T extends TkBasePo> void buildCreateUserInfo(T po) {
        UserContext userContext = UserContext.getInstance();
        po.setCreateBy(userContext.getUserName());
        po.setLastUpdateBy(userContext.getUserName());
        po.setCreationDate(new Date());
        po.setLastUpdateDate(new Date());
    }

    public static <T extends TkBasePo> void buildUpdateUserInfo(T po) {
        UserContext userContext = UserContext.getInstance();
        po.setLastUpdateBy(userContext.getUserName());
        po.setLastUpdateDate(new Date());
    }
}
