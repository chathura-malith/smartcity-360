package com.smartcity.pattern.decorator;

public class CompressionDecorator extends StreamDecorator {

    public CompressionDecorator(DataStream stream) {
        super(stream);
    }

    @Override
    public String processData() {
        String data = super.processData();
        return "COMPRESSED_GZIP(" + data + ")";
    }
}