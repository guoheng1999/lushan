package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVO {
    private String password;
    private String email;
    private String phone;
    private String organization;
    private String realName;
    private String userEdu;
}