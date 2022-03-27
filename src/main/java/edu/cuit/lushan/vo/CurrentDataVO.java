package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentDataVO {
    private Integer deviceId;
    private Integer dataLevel;
    private String fileType;
    private String logTime;
    private String dataName;
    private String path;
}
