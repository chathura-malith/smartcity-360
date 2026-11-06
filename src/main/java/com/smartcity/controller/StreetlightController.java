package com.smartcity.controller;

import com.smartcity.dto.request.StreetlightRequestDto;
import com.smartcity.dto.response.StreetlightResponseDto;
import com.smartcity.service.StreetlightService;
import com.smartcity.util.StandardResponseDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/streetlights")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StreetlightController {

    @Inject
    private StreetlightService streetlightService;

    @GET
    public Response getAllStreetlights() {
        try {
            List<StreetlightResponseDto> lights = streetlightService.getAllStreetlights();
            return Response.ok(new StandardResponseDto(
                    200, "Streetlights loaded successfully", lights)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new StandardResponseDto(
                            500, "Error loading streetlights", null)).build();
        }
    }

    @POST
    public Response addStreetlight(StreetlightRequestDto requestDto) {
        try {
            StreetlightResponseDto createdLight = streetlightService.addStreetlight(requestDto);
            return Response.status(Response.Status.CREATED)
                    .entity(new StandardResponseDto(
                            201, "Smart Streetlight added successfully!", createdLight)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new StandardResponseDto(
                            400, e.getMessage(), null)).build();
        }
    }
}