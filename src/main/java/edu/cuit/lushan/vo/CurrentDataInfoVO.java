package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentDataInfoVO {
    private Integer deviceId;
    private Integer dataLevel;
    private String fileType;
    private String logTime;
    private String dataName;
    private Integer year;
    private Integer month;
    private Integer day;
}
