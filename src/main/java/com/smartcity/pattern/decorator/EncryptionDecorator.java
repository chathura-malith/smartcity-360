package com.smartcity.pattern.decorator;

import java.util.Base64;

public class EncryptionDecorator extends StreamDecorator {

    public EncryptionDecorator(DataStream stream) {
        super(stream);
    }

    @Override
    public String processData() {
        String data = super.processData();
        return "ENCRYPTED(" + Base64.getEncoder().encodeToString(data.getBytes()) + ")";
    }
}