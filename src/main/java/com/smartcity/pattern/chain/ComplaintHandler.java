package com.smartcity.pattern.chain;

import com.smartcity.model.Complaint;

public abstract class ComplaintHandler {
    protected ComplaintHandler nextHandler;

    public void setNextHandler(ComplaintHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleComplaint(Complaint complaint);
}
