package com.xlt.constant;

public class Constant {

    public static enum Status{

        SUCCESS(1,"成功"),
        FAIL(2,"失败");
        private final int value;
        private final String description;

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        private Status(int value, String description){
           this.value=value;
           this.description=description;
        }
    }
}
