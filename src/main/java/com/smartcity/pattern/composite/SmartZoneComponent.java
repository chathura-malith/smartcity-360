package com.smartcity.pattern.composite;

import com.smartcity.model.enums.DeviceStatus;

public interface SmartZoneComponent {
    void shutdownPower();
    double reportUsage();
    DeviceStatus getPowerStatus();
}