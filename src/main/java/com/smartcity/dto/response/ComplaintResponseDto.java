package com.smartcity.dto.response;

import com.smartcity.model.enums.ComplaintSeverity;
import com.smartcity.model.enums.ComplaintStatus;
import com.smartcity.model.enums.HandlerRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseDto {
    private Long id;
    private String complaintCode;
    private String description;
    private ComplaintSeverity severity;
    private ComplaintStatus status;
    private HandlerRole assignedHandler;
    private Timestamp createdAt;
}