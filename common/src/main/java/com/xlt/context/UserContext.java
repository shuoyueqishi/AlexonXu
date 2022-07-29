package com.xlt.context;

import com.xlt.model.vo.UserInfoVo;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class UserContext {

    /**
     * 存储用户信息
     */
    private static FastThreadLocal<UserInfoVo> userInfoHolder = new FastThreadLocal();

   public static void setUserInfo(UserInfoVo userInfoVo) {
       userInfoHolder.set(userInfoVo);
   }

   public static UserInfoVo getUserInfo() {
       return userInfoHolder.get();
   }

   public static void remove(){
       userInfoHolder.remove();
   }

   public static String getUserName() {
       UserInfoVo userInfoVo = userInfoHolder.get();
       return userInfoVo==null?"DefaultUser":userInfoVo.getName();
   }

   public static Long getUserId() {
       UserInfoVo userInfoVo = userInfoHolder.get();
       return userInfoVo==null?-1L:userInfoVo.getUserId();
   }
}
