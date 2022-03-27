package edu.cuit.lushan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.models.auth.In;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Guoheng
 * @since 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    /**
     * {0：0级数据 ， 1：1级数据}
     */
    private Integer dataLevel;

    /**
     * nc， csv
     */
    private String fileType;

    private String logTime;

    private String dataName;

    private String path;

    private Integer year;

    private Integer month;

    private Integer day;

    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;


}
