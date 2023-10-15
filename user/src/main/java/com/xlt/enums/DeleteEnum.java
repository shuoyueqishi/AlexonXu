package com.xlt.enums;

public enum DeleteEnum {

    NOT_DELETE(0,"Not Deleted"),
    DELETED(1,"Deleted");

   private String desc;
   private Integer code;

   DeleteEnum(Integer code, String desc) {
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
        DeleteEnum[] typeEnums = DeleteEnum.values();
        for(int i=0;i<typeEnums.length;i++) {
            DeleteEnum typeEnum = typeEnums[i];
            if(typeEnum.code.equals(code)) {
                return typeEnum.desc;
            }
        }
        return "";
    }
}
