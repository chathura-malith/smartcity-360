package com.smartcity.pattern.decorator;

public abstract class StreamDecorator implements DataStream {
    protected DataStream wrappedStream;

    public StreamDecorator(DataStream stream) {
        this.wrappedStream = stream;
    }

    @Override
    public String processData() {
        return wrappedStream.processData();
    }
}