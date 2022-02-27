package edu.cuit.lushan.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志详情表
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLogDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志详情ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 日志ID，对应日志表中的ID
     */
    private String tableName;

    /**
     * 旧值
     */
    private String oldValue;

    /**
     * 新值
     */
    private String newValue;

    /**
     * 备注
     */
    private String comment;

    /**
     * 记录时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;


}
