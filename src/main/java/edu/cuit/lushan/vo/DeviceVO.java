package edu.cuit.lushan.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceVO {
    private String deviceName;
    private String description;
    private Integer id;
}
