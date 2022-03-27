package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewedInfoVO {
    private Integer id;
    private String phone;
    private String organization;
    private String email;
    private String name;
    private LocalDateTime modifyTime;
    private Integer roleId;
}
