package edu.cuit.lushan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-27
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 组织机构
     */
    private String organization;

    /**
     * 帐号状态
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer accountStatus;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(select = false, fill = FieldFill.INSERT)
    private Integer isDelete;

    /**
     * 用户学历
     */
    private String userEdu;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户角色ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer roleId;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 修改人ID
     */
    private Integer modifyUserId;

}
