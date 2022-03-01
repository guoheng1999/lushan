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
 * @since 2022-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 请求下载的用户ID
     */
    private Integer downloadUserId;

    /**
     * 请求下载的地址
     */
    private String downloadIp;

    /**
     * 下载时间
     */
    private LocalDateTime downloadTime;

    /**
     * 下载的文件的ID
     */
    private String downloadFileName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;
    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;


}
