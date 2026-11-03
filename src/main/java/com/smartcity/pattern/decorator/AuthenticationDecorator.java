package com.smartcity.pattern.decorator;

public class AuthenticationDecorator extends StreamDecorator {

    public AuthenticationDecorator(DataStream stream) {
        super(stream);
    }

    @Override
    public String processData() {
        String data = super.processData();
        return "AUTH_VERIFIED[" + data + " | AUTH_KEY_AUTH9928]";
    }
}