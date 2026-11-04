package com.smartcity.controller;

import com.smartcity.dto.request.ZoneRequestDto;
import com.smartcity.dto.response.ZoneResponseDto;
import com.smartcity.service.ZoneService;
import com.smartcity.util.StandardResponseDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/zones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ZoneController {

    @Inject
    private ZoneService zoneService;

    @GET
    public Response getAllZones() {
        try {
            List<ZoneResponseDto> zones = zoneService.getAllZones();
            return Response.ok(new StandardResponseDto(
                    200, "Zones loaded successfully", zones)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(500, "Error loading zones", null)).build();
        }
    }

    @POST
    public Response createZone(ZoneRequestDto requestDto) {
        try {
            ZoneResponseDto createdZone = zoneService.createZone(requestDto);
            return Response.status(Response.Status.CREATED)
                    .entity(new StandardResponseDto(
                            201, "Zone created successfully!", createdZone)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new StandardResponseDto(400, e.getMessage(), null)).build();
        }
    }

    @POST
    @Path("/{id}/shutdown")
    public Response shutdownZonePower(@PathParam("id") Long zoneId) {
        try {
            zoneService.shutdownZonePower(zoneId);
            return Response.ok(new StandardResponseDto(200, "Power shutdown propagated successfully!", null)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(
                            500, "Failed to shutdown power", null)).build();
        }
    }

    @GET
    @Path("/{id}/usage")
    public Response getZoneUsage(@PathParam("id") Long zoneId) {
        try {
            double totalUsage = zoneService.getZoneUsage(zoneId);
            return Response.ok(new StandardResponseDto(
                    200, "Usage calculated successfully!", totalUsage)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(
                            500, "Failed to calculate usage", null)).build();
        }
    }
}