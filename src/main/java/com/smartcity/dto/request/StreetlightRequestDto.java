package com.smartcity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetlightRequestDto {
    private String modelName;
    private Integer wattage;
    private String colorTemperature;

    private Double latitude;
    private Double longitude;
}