package com.smartcity.model;

import com.smartcity.model.enums.ComplaintSeverity;
import com.smartcity.model.enums.ComplaintStatus;
import com.smartcity.model.enums.HandlerRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complaints")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "complaint_code", nullable = false, unique = true, length = 50)
    private String complaintCode;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 20)
    private ComplaintSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ComplaintStatus status = ComplaintStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "assigned_handler", length = 50)
    private HandlerRole assignedHandler = HandlerRole.LOCAL_STAFF;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}