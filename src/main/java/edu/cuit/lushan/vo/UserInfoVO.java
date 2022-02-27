package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    private String email;
    private String headImg;
    private String phone;
    private String realName;
}
