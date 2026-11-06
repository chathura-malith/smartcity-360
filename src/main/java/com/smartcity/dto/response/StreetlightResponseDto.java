package com.smartcity.dto.response;

import com.smartcity.model.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetlightResponseDto {
    private Long id;
    private String modelName;
    private Integer wattage;
    private String colorTemperature;
    private Double latitude;
    private Double longitude;
    private DeviceStatus status;
}