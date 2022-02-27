package edu.cuit.lushan.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志表
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
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，日志标识
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 方法名，包含包名类名
     */
    private String methodName;
    /**
     * http请求方法
     */
    private String httpMethod;

    /**
     * 请求url
     */
    private String requestUrl;
    /**
     * 调用IP
     */
    private String ip;

    /**
     * 请求参数，json存储
     */
    private String requestParam;

    /**
     * 响应结果，json
     */
    private String responseData;

    /**
     * 请求时间
     */
    private LocalDateTime requestTime;

    /**
     * 响应码
     */
    private Integer responseCode;

    /**
     * 操作用户ID
     */
    private String userId;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(select = false, fill = FieldFill.INSERT)
    private Integer isDelete;
}
