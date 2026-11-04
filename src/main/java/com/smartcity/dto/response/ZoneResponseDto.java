package com.smartcity.dto.response;

import com.smartcity.model.enums.DeviceStatus;
import com.smartcity.model.enums.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponseDto {
    private Long id;
    private String name;
    private ZoneType type;
    private Long parentId;
    private String parentName;
    private DeviceStatus powerStatus;
    private Double energyConsumption;
    private List<ZoneResponseDto> subZones;
}