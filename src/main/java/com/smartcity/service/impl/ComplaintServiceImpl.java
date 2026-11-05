package com.smartcity.service.impl;

import com.smartcity.dto.request.ComplaintRequestDto;
import com.smartcity.dto.response.ComplaintResponseDto;
import com.smartcity.model.Complaint;
import com.smartcity.model.enums.ComplaintStatus;
import com.smartcity.pattern.chain.CityManagerHandler;
import com.smartcity.pattern.chain.ComplaintHandler;
import com.smartcity.pattern.chain.LocalStaffHandler;
import com.smartcity.pattern.chain.SupervisorHandler;
import com.smartcity.service.ComplaintService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ComplaintServiceImpl implements ComplaintService {

    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    @Override
    @Transactional
    public ComplaintResponseDto submitComplaint(ComplaintRequestDto requestDto) {
        Complaint complaint = new Complaint();
        complaint.setComplaintCode("CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        complaint.setDescription(requestDto.getDescription());
        complaint.setSeverity(requestDto.getSeverity());
        complaint.setStatus(ComplaintStatus.PENDING);

        ComplaintHandler localStaff = new LocalStaffHandler();
        ComplaintHandler supervisor = new SupervisorHandler();
        ComplaintHandler cityManager = new CityManagerHandler();


        localStaff.setNextHandler(supervisor);
        supervisor.setNextHandler(cityManager);

        localStaff.handleComplaint(complaint);

        entityManager.persist(complaint);
        entityManager.flush();

        return mapToResponseDto(complaint);
    }

    @Override
    public List<ComplaintResponseDto> getAllComplaints() {
        List<Complaint> complaints = entityManager.createQuery(
                "SELECT c FROM Complaint c ORDER BY c.id DESC", Complaint.class)
                .getResultList();

        return complaints.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private ComplaintResponseDto mapToResponseDto(Complaint complaint) {
        return ComplaintResponseDto.builder()
                .id(complaint.getId())
                .complaintCode(complaint.getComplaintCode())
                .description(complaint.getDescription())
                .severity(complaint.getSeverity())
                .status(complaint.getStatus())
                .assignedHandler(complaint.getAssignedHandler())
                .createdAt(complaint.getCreatedAt())
                .build();
    }
}