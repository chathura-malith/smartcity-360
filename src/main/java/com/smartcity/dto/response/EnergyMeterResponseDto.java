package com.smartcity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyMeterResponseDto {
    private Long id;
    private String meterCode;
    private Long zoneId;
    private String zoneName;
    private Double rawReadingKwh;
    private Boolean isEncrypted;
    private Boolean isAuthenticated;
    private Boolean isCompressed;
    private String processedPayload;
    private Timestamp lastUpdated;
}