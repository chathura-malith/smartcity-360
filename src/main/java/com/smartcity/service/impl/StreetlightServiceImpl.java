package com.smartcity.service.impl;

import com.smartcity.dto.request.StreetlightRequestDto;
import com.smartcity.dto.response.StreetlightResponseDto;
import com.smartcity.model.Streetlight;
import com.smartcity.model.StreetlightType;
import com.smartcity.model.enums.DeviceStatus;
import com.smartcity.pattern.flyweight.StreetlightTypeFactory;
import com.smartcity.service.StreetlightService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StreetlightServiceImpl implements StreetlightService {

    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    @Inject
    private StreetlightTypeFactory typeFactory;

    @Override
    @Transactional
    public StreetlightResponseDto addStreetlight(StreetlightRequestDto requestDto) {

        StreetlightType sharedType = typeFactory.getStreetlightType(
                requestDto.getModelName(),
                requestDto.getWattage(),
                requestDto.getColorTemperature()
        );

        Streetlight streetlight = new Streetlight();
        streetlight.setStreetlightType(sharedType);
        streetlight.setLatitude(requestDto.getLatitude());
        streetlight.setLongitude(requestDto.getLongitude());
        streetlight.setStatus(DeviceStatus.ON);

        entityManager.persist(streetlight);
        entityManager.flush();

        return mapToResponseDto(streetlight);
    }

    @Override
    public List<StreetlightResponseDto> getAllStreetlights() {
        List<Streetlight> streetlights = entityManager.createQuery(
                        "SELECT s FROM Streetlight s JOIN FETCH s.streetlightType" +
                                " ORDER BY s.id DESC", Streetlight.class)
                .getResultList();

        return streetlights.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private StreetlightResponseDto mapToResponseDto(Streetlight streetlight) {
        return StreetlightResponseDto.builder()
                .id(streetlight.getId())
                .modelName(streetlight.getStreetlightType().getModelName())
                .wattage(streetlight.getStreetlightType().getWattage())
                .colorTemperature(streetlight.getStreetlightType().getColorTemperature())
                .latitude(streetlight.getLatitude())
                .longitude(streetlight.getLongitude())
                .status(streetlight.getStatus())
                .build();
    }
}