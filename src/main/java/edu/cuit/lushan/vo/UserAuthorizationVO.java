package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthorizationVO {
    private Integer id;
    private Integer accountStatus;
    private Integer roleId;
}
