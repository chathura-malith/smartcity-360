package com.smartcity.service;

import com.smartcity.dto.request.EnergyMeterProcessRequestDto;
import com.smartcity.dto.response.EnergyMeterResponseDto;

import java.util.List;

public interface EnergyMeterService {
    EnergyMeterResponseDto processAndSaveMeterData(EnergyMeterProcessRequestDto requestDto);
    List<EnergyMeterResponseDto> getAllMeters();
}