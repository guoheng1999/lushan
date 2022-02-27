package edu.cuit.lushan.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataFileVO {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 数据名称，展示给用户看的名字
     */
    private String dataName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 观测设备
     */
    private Integer deviceId;
}
