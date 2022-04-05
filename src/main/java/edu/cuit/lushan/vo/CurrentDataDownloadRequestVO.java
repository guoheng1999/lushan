package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentDataDownloadRequestVO {
    private Integer dataLevel;
    private Integer deviceId;
    private String fromDay;
    private String endDay;
    private String email;
    private Integer dataType;
    private String fileFormat;
}
