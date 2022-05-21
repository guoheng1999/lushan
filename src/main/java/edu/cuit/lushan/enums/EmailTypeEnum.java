package edu.cuit.lushan.enums;

public enum EmailTypeEnum {
    /**
     * 邮件类型
     */
    REGISTER_SUCCESS(0, "审核通过"),
    REGISTER_FAILURE(1, "审核不通过"),
    CHANGE_PASSWORD(2, "修改密码"),
    DATA_PACKAGE(3, "数据打包");

    private Integer code;
    private String msg;

    EmailTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
