package com.smartcity.service.impl;

import com.smartcity.dto.request.ZoneRequestDto;
import com.smartcity.dto.response.ZoneResponseDto;
import com.smartcity.model.Zone;
import com.smartcity.model.enums.DeviceStatus;
import com.smartcity.pattern.composite.BuildingZone;
import com.smartcity.pattern.composite.CompositeZone;
import com.smartcity.pattern.composite.SmartZoneComponent;
import com.smartcity.service.ZoneService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ZoneServiceImpl implements ZoneService {

    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public ZoneResponseDto createZone(ZoneRequestDto requestDto) {
        Zone zone = new Zone();
        zone.setName(requestDto.getName());
        zone.setType(requestDto.getType());
        zone.setPowerStatus(requestDto.getPowerStatus() != null ? requestDto.getPowerStatus() : DeviceStatus.ON);
        zone.setEnergyConsumption(requestDto.getEnergyConsumption() != null ? requestDto.getEnergyConsumption() : 0.0);

        if (requestDto.getParentId() != null) {
            Zone parent = entityManager.find(Zone.class, requestDto.getParentId());
            if (parent != null) {
                parent.addSubZone(zone);
            } else {
                throw new IllegalArgumentException("Parent Zone not found!");
            }
        }

        entityManager.persist(zone);
        entityManager.flush();
        return mapToResponseDto(zone);
    }

    @Override
    @Transactional
    public List<ZoneResponseDto> getAllZones() {
        List<Zone> zones = entityManager.createQuery(
                        "SELECT DISTINCT z FROM Zone z LEFT JOIN FETCH z.subZones WHERE z.parentZone IS NULL",
                        Zone.class)
                .getResultList();

        return zones.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void shutdownZonePower(Long zoneId) {
        Zone rootZoneEntity = entityManager.find(Zone.class, zoneId);
        if (rootZoneEntity == null) throw new IllegalArgumentException("Zone not found");

        DeviceStatus targetStatus = (rootZoneEntity.getPowerStatus() == DeviceStatus.ON) ?
                DeviceStatus.OFF : DeviceStatus.ON;

        SmartZoneComponent compositeTree = buildCompositeTree(rootZoneEntity);

        if (targetStatus == DeviceStatus.OFF) {
            compositeTree.shutdownPower();
        } else {
            rootZoneEntity.setPowerStatus(DeviceStatus.ON);
        }

        syncPowerStatusToDb(rootZoneEntity, compositeTree, targetStatus == DeviceStatus.OFF);
        entityManager.merge(rootZoneEntity);
    }

    @Override
    @Transactional
    public double getZoneUsage(Long zoneId) {
        Zone rootZoneEntity = entityManager.find(Zone.class, zoneId);
        if (rootZoneEntity == null) throw new IllegalArgumentException("Zone not found");

        SmartZoneComponent compositeTree = buildCompositeTree(rootZoneEntity);
        return compositeTree.reportUsage();
    }

    private SmartZoneComponent buildCompositeTree(Zone entity) {
        if (entity.getSubZones() != null) {
            entity.getSubZones().size();
        }

        if (entity.getSubZones() == null || entity.getSubZones().isEmpty()) {
            double usage = entity.getEnergyConsumption() != null ? entity.getEnergyConsumption() : 0.0;
            return new BuildingZone(entity.getId(), entity.getName(), entity.getPowerStatus(), usage);
        } else {
            CompositeZone composite = new CompositeZone(entity.getId(), entity.getName(), entity.getPowerStatus());
            for (Zone sub : entity.getSubZones()) {
                composite.addSubZone(buildCompositeTree(sub));
            }
            return composite;
        }
    }

    private void syncPowerStatusToDb(Zone entity, SmartZoneComponent component, boolean isShutdown) {
        if (isShutdown) {
            entity.setPowerStatus(component.getPowerStatus());
        } else {
            entity.setPowerStatus(DeviceStatus.ON);
        }

        if (component instanceof CompositeZone) {
            CompositeZone compositeZone = (CompositeZone) component;
            List<Zone> subEntities = entity.getSubZones();
            List<SmartZoneComponent> subComponents = compositeZone.getSubZones();

            for (int i = 0; i < subEntities.size(); i++) {
                if (i < subComponents.size()) {
                    syncPowerStatusToDb(subEntities.get(i), subComponents.get(i), isShutdown);
                }
            }
        }
    }

    private ZoneResponseDto mapToResponseDto(Zone zone) {
        return ZoneResponseDto.builder()
                .id(zone.getId())
                .name(zone.getName())
                .type(zone.getType())
                .powerStatus(zone.getPowerStatus())
                .energyConsumption(zone.getEnergyConsumption() != null ? zone.getEnergyConsumption() : 0.0)
                .parentId(zone.getParentZone() != null ? zone.getParentZone().getId() : null)
                .parentName(zone.getParentZone() != null ? zone.getParentZone().getName() : null)
                .subZones(zone.getSubZones() != null && !zone.getSubZones().isEmpty() ?
                        zone.getSubZones().stream().map(this::mapToResponseDto).collect(Collectors.toList()) : null)
                .build();
    }
}