package com.alexon.authorization.utils;


import com.alexon.authorization.context.UserContext;
import com.alexon.model.po.BasePo;

import java.util.Date;

public class PoUtil {
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

    public static Long getCurUserId() {
        return UserContext.getUserId();
    }
}
