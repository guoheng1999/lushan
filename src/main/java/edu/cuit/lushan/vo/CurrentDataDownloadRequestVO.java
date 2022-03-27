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
    private Integer deviceId;
    private Integer dataLevel;
    private String fromDay;
    private String endDay;
    private String email;
}
