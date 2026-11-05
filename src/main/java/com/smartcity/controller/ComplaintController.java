package com.smartcity.controller;

import com.smartcity.dto.request.ComplaintRequestDto;
import com.smartcity.dto.response.ComplaintResponseDto;
import com.smartcity.service.ComplaintService;
import com.smartcity.util.StandardResponseDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/complaints")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ComplaintController {

    @Inject
    private ComplaintService complaintService;

    @GET
    public Response getAllComplaints() {
        try {
            List<ComplaintResponseDto> complaints = complaintService.getAllComplaints();
            return Response.ok(new StandardResponseDto(200, "Complaints loaded successfully", complaints)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(500, "Error loading complaints", null)).build();
        }
    }

    @POST
    public Response submitComplaint(ComplaintRequestDto requestDto) {
        try {
            ComplaintResponseDto createdComplaint = complaintService.submitComplaint(requestDto);
            return Response.status(Response.Status.CREATED)
                    .entity(new StandardResponseDto(201, "Complaint escalated and processed successfully!", createdComplaint)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new StandardResponseDto(400, e.getMessage(), null)).build();
        }
    }
}