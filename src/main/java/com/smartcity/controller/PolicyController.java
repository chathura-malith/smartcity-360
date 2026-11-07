package com.smartcity.controller;

import com.smartcity.dto.request.PolicyEvaluateRequestDto;
import com.smartcity.dto.request.PolicyRequestDto;
import com.smartcity.dto.response.PolicyResponseDto;
import com.smartcity.service.PolicyService;
import com.smartcity.util.StandardResponseDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/policies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PolicyController {

    @Inject
    private PolicyService policyService;

    @GET
    public Response getAllPolicies() {
        try {
            List<PolicyResponseDto> policies = policyService.getAllPolicies();
            return Response.ok(
                    new StandardResponseDto(200, "Policies loaded successfully", policies)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(
                            new StandardResponseDto(500, "Error loading policies", null)).build();
        }
    }

    @POST
    public Response createPolicy(PolicyRequestDto requestDto) {
        try {
            PolicyResponseDto createdPolicy = policyService.createPolicy(requestDto);
            return Response.status(Response.Status.CREATED)
                    .entity(
                            new StandardResponseDto(201,
                                    "City Policy defined successfully!",
                                    createdPolicy)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new StandardResponseDto(400, e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("/evaluate")
    public Response evaluatePolicy(PolicyEvaluateRequestDto evaluateDto) {
        try {
            boolean isTriggered = policyService.evaluatePolicy(evaluateDto);
            String msg = isTriggered
                    ? "Policy Rule VIOLATED / TRIGGERED!"
                    : "Policy Conditions Normal (Not Triggered)";

            return Response.ok(new StandardResponseDto(200, msg, isTriggered)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new StandardResponseDto(400, e.getMessage(), null)).build();
        }
    }
}