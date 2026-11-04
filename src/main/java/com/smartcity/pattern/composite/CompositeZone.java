package com.smartcity.pattern.composite;

import com.smartcity.model.enums.DeviceStatus;
import java.util.ArrayList;
import java.util.List;

public class CompositeZone implements SmartZoneComponent {
    private Long id;
    private String name;
    private DeviceStatus powerStatus;
    private List<SmartZoneComponent> subZones = new ArrayList<>();

    public CompositeZone(Long id, String name, DeviceStatus powerStatus) {
        this.id = id;
        this.name = name;
        this.powerStatus = powerStatus;
    }

    public void addSubZone(SmartZoneComponent zone) {
        subZones.add(zone);
    }

    public List<SmartZoneComponent> getSubZones() {
        return subZones;
    }

    @Override
    public void shutdownPower() {
        this.powerStatus = DeviceStatus.OFF;
        for (SmartZoneComponent zone : subZones) {
            zone.shutdownPower();
        }
    }

    @Override
    public double reportUsage() {
        double totalUsage = 0.0;
        for (SmartZoneComponent zone : subZones) {
            totalUsage += zone.reportUsage();
        }
        return totalUsage;
    }

    @Override
    public DeviceStatus getPowerStatus() {
        return powerStatus;
    }
}