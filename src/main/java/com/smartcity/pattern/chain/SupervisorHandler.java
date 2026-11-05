package com.smartcity.pattern.chain;

import com.smartcity.model.Complaint;
import com.smartcity.model.enums.ComplaintSeverity;
import com.smartcity.model.enums.ComplaintStatus;
import com.smartcity.model.enums.HandlerRole;

public class SupervisorHandler extends ComplaintHandler {

    @Override
    public void handleComplaint(Complaint complaint) {
        if (complaint.getSeverity() == ComplaintSeverity.MEDIUM) {
            complaint.setAssignedHandler(HandlerRole.SUPERVISOR);
            complaint.setStatus(ComplaintStatus.RESOLVED);
            System.out.println("Supervisor handled the complaint: " + complaint.getComplaintCode());
        }
        else if (nextHandler != null) {
            System.out.println("Supervisor escalating complaint to City Manager...");
            complaint.setStatus(ComplaintStatus.ESCALATED);
            nextHandler.handleComplaint(complaint);
        }
    }
}
