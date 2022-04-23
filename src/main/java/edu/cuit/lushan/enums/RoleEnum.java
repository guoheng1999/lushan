package edu.cuit.lushan.enums;

import lombok.Data;

public enum RoleEnum {
    USER(0, "USER"), VIP(1, "VIP"), MANAGER(2, "MANAGER"), ADMIN(3, "ADMIN");
    private Integer code;
    private String name;
    RoleEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }
    public Integer getCode(){
        return this.code;
    }
    public String getName(){
        return this.name;
    }
}
