package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryXlsxDataInfoVO {
    private String dataName;
    private String logTime;
    private String station;
    private String pages;
    private String type;
}
