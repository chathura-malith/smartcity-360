package com.smartcity.service;

import com.smartcity.dto.request.ZoneRequestDto;
import com.smartcity.dto.response.ZoneResponseDto;

import java.util.List;

public interface ZoneService {
    ZoneResponseDto createZone(ZoneRequestDto requestDto);
    List<ZoneResponseDto> getAllZones();
    void shutdownZonePower(Long zoneId);
    double getZoneUsage(Long zoneId);
}