package com.smartcity.dto.request;

import com.smartcity.model.enums.DeviceStatus;
import com.smartcity.model.enums.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRequestDto {
    private String name;
    private ZoneType type;
    private Long parentId;
    private DeviceStatus powerStatus;
    private Double energyConsumption;
}