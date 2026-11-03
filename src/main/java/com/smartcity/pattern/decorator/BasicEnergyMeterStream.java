package com.smartcity.pattern.decorator;

public class BasicEnergyMeterStream implements DataStream {
    private final String rawData;

    public BasicEnergyMeterStream(String rawData) {
        this.rawData = rawData;
    }

    @Override
    public String processData() {
        return "RAW_DATA[" + rawData + "]";
    }
}