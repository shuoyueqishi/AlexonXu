package com.xlt.enums;

public enum SystemConfigTypeEnum {

    STRING(1,"String"),
    JSON(2,"JSON");

   private String desc;
   private Integer code;

   SystemConfigTypeEnum(Integer code,String desc) {
       this.code = code;
       this.desc = desc;
   }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }

    public static String getDescByCode(Integer code) {
        SystemConfigTypeEnum[] typeEnums = SystemConfigTypeEnum.values();
        for(int i=0;i<typeEnums.length;i++) {
            SystemConfigTypeEnum typeEnum = typeEnums[i];
            if(typeEnum.code.equals(code)) {
                return typeEnum.desc;
            }
        }
        return "";
    }
}
