package com.smartcity.controller;

import com.smartcity.dto.request.EnergyMeterProcessRequestDto;
import com.smartcity.dto.response.EnergyMeterResponseDto;
import com.smartcity.service.EnergyMeterService;
import com.smartcity.util.StandardResponseDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/energy-meters")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnergyMeterController {

    @Inject
    private EnergyMeterService energyMeterService;

    @POST
    @Path("/process")
    public Response processMeterData(EnergyMeterProcessRequestDto requestDto) {
        try {
            EnergyMeterResponseDto responseDto = energyMeterService.processAndSaveMeterData(requestDto);
            return Response.status(Response.Status.CREATED)
                    .entity(new StandardResponseDto(
                            201,
                            "Energy meter data processed successfully using Decorator Pattern!",
                            responseDto)
                    )
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(
                            500,
                            "Error processing meter data: " + e.getMessage(),
                            null)
                    )
                    .build();
        }
    }

    @GET
    public Response getAllEnergyMeters() {
        try {
            List<EnergyMeterResponseDto> meters = energyMeterService.getAllMeters();
            return Response.ok(
                    new StandardResponseDto(
                            200, "Fetched all energy meter records successfully!", meters
                    )
            ).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(
                            500, "Error fetching meters: " + e.getMessage(), null)
                    )
                    .build();
        }
    }
}