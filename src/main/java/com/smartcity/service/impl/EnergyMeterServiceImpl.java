package com.smartcity.service.impl;

import com.smartcity.dto.request.EnergyMeterProcessRequestDto;
import com.smartcity.dto.response.EnergyMeterResponseDto;
import com.smartcity.model.EnergyMeter;
import com.smartcity.model.Zone;
import com.smartcity.pattern.decorator.*;
import com.smartcity.service.EnergyMeterService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EnergyMeterServiceImpl implements EnergyMeterService {

    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public EnergyMeterResponseDto processAndSaveMeterData(EnergyMeterProcessRequestDto requestDto) {

        Zone zone = entityManager.find(Zone.class, requestDto.getZoneId());
        if (zone == null) {
            throw new IllegalArgumentException("Zone not found with ID: " + requestDto.getZoneId());
        }

        String rawDataString = "Reading: " + requestDto.getRawReadingKwh() + " kWh";
        DataStream stream = new BasicEnergyMeterStream(rawDataString);

        if (Boolean.TRUE.equals(requestDto.getIsCompressed())) {
            stream = new CompressionDecorator(stream);
        }
        if (Boolean.TRUE.equals(requestDto.getIsAuthenticated())) {
            stream = new AuthenticationDecorator(stream);
        }
        if (Boolean.TRUE.equals(requestDto.getIsEncrypted())) {
            stream = new EncryptionDecorator(stream);
        }

        String processedPayload = stream.processData();

        EnergyMeter meter = new EnergyMeter();
        meter.setMeterCode(requestDto.getMeterCode());
        meter.setZone(zone);
        meter.setRawReadingKwh(requestDto.getRawReadingKwh());
        meter.setIsEncrypted(requestDto.getIsEncrypted());
        meter.setIsAuthenticated(requestDto.getIsAuthenticated());
        meter.setIsCompressed(requestDto.getIsCompressed());
        meter.setProcessedPayload(processedPayload);

        entityManager.persist(meter);
        entityManager.flush();

        return mapToResponseDto(meter);
    }

    @Override
    public List<EnergyMeterResponseDto> getAllMeters() {
        List<EnergyMeter> meters = entityManager.createQuery("SELECT e FROM EnergyMeter e", EnergyMeter.class)
                .getResultList();

        return meters.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private EnergyMeterResponseDto mapToResponseDto(EnergyMeter meter) {
        return EnergyMeterResponseDto.builder()
                .id(meter.getId())
                .meterCode(meter.getMeterCode())
                .zoneId(meter.getZone() != null ? meter.getZone().getId() : null)
                .zoneName(meter.getZone() != null ? meter.getZone().getName() : "N/A")
                .rawReadingKwh(meter.getRawReadingKwh())
                .isEncrypted(meter.getIsEncrypted())
                .isAuthenticated(meter.getIsAuthenticated())
                .isCompressed(meter.getIsCompressed())
                .processedPayload(meter.getProcessedPayload())
                .lastUpdated(meter.getLastUpdated())
                .build();
    }
}