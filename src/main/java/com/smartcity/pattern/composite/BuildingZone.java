package com.smartcity.pattern.composite;

import com.smartcity.model.enums.DeviceStatus;

public class BuildingZone implements SmartZoneComponent {
    private Long id;
    private String name;
    private DeviceStatus powerStatus;
    private Double energyConsumption;

    public BuildingZone(Long id, String name, DeviceStatus powerStatus, Double energyConsumption) {
        this.id = id;
        this.name = name;
        this.powerStatus = powerStatus;
        this.energyConsumption = energyConsumption;
    }

    @Override
    public void shutdownPower() {
        this.powerStatus = DeviceStatus.OFF;
    }

    @Override
    public double reportUsage() {
        return this.energyConsumption != null ? this.energyConsumption : 0.0;
    }

    @Override
    public DeviceStatus getPowerStatus() {
        return powerStatus;
    }
}