package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryXlsxDataVO {
    private String dataName;
    private String logTime;
    private String station;
    private String pages;
    private String path;
    private String type;
}
