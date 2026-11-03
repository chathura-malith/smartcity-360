package com.smartcity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyMeterProcessRequestDto {
    private String meterCode;
    private Long zoneId;
    private Double rawReadingKwh;
    private Boolean isEncrypted;
    private Boolean isAuthenticated;
    private Boolean isCompressed;
}