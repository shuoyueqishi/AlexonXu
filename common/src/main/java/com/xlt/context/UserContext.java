package com.xlt.context;

import com.xlt.model.vo.UserInfoVo;

public class UserContext {

    /**
     * 存储用户信息
     */
    private ThreadLocal<UserInfoVo> userVoThreadLocal;

    /**
     * 保证单例（内存可见，禁止指令重排）
     */
    volatile private static UserContext instance;

    /**
     * 单例获取实例
     *
     * @return 返回用户上下文实例
     */
    public static UserContext getInstance() {
        if (instance == null) {
            synchronized (UserContext.class) {
                if (instance == null) {
                    instance = new UserContext();
                    UserInfoVo userInfoVo = UserInfoVo.builder().userId(-1L).currentRole("Guest").name("Sys").build();
                    instance.setUserContext(userInfoVo);
                }
            }
        }
        return instance;
    }

    public UserContext() {
        userVoThreadLocal = new ThreadLocal<>();
    }

    public void setUserContext(UserInfoVo userInfoVo) {
        userVoThreadLocal.set(userInfoVo);
    }

    public UserInfoVo getUserContext() {
        return userVoThreadLocal.get();
    }

    public void removeUserContext() {
        userVoThreadLocal.remove();
    }

    public String getUserName() {
        return userVoThreadLocal.get().getName();
    }

    public Long getUserId(){
        return userVoThreadLocal.get().getUserId();
    }

}
