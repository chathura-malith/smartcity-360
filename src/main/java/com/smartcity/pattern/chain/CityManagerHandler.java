package com.smartcity.pattern.chain;

import com.smartcity.model.Complaint;
import com.smartcity.model.enums.ComplaintSeverity;
import com.smartcity.model.enums.ComplaintStatus;
import com.smartcity.model.enums.HandlerRole;

public class CityManagerHandler extends ComplaintHandler {

    @Override
    public void handleComplaint(Complaint complaint) {
        if (complaint.getSeverity() == ComplaintSeverity.HIGH) {
            complaint.setAssignedHandler(HandlerRole.CITY_MANAGER);
            complaint.setStatus(ComplaintStatus.RESOLVED);
            System.out.println("City Manager handled the HIGH severity complaint: " + complaint.getComplaintCode());
        } else {
            System.out.println("Unhandled complaint severity for: " + complaint.getComplaintCode());
        }
    }
}