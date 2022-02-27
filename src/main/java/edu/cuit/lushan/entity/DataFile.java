package edu.cuit.lushan.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据文件表
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
public class DataFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名
     */
    @TableField(fill = FieldFill.INSERT)
    private String fileName;

    /**
     * 数据名称，展示给用户看的名字
     */
    private String dataName;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 修改人ID
     */
    private Integer modifyUserId;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 观测设备
     */
    private Integer deviceId;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;


}
