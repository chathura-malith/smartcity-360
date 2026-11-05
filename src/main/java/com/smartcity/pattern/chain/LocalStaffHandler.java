package com.smartcity.pattern.chain;
import com.smartcity.model.Complaint;
import com.smartcity.model.enums.ComplaintSeverity;
import com.smartcity.model.enums.ComplaintStatus;
import com.smartcity.model.enums.HandlerRole;

public class LocalStaffHandler extends ComplaintHandler {

    @Override
    public void handleComplaint(Complaint complaint) {
        if (complaint.getSeverity() == ComplaintSeverity.LOW) {
            complaint.setAssignedHandler(HandlerRole.LOCAL_STAFF);
            complaint.setStatus(ComplaintStatus.RESOLVED);
            System.out.println("Local Staff handled the complaint: " + complaint.getComplaintCode());
        }
        else if (nextHandler != null) {
            System.out.println("Local Staff escalating complaint to Supervisor...");
            complaint.setStatus(ComplaintStatus.ESCALATED);
            nextHandler.handleComplaint(complaint);
        }
    }
}
