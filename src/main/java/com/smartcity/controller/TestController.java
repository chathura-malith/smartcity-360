package com.smartcity.controller;

import com.smartcity.util.StandardResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestController {
    @PersistenceContext(unitName = "SmartCityPU")
    private EntityManager entityManager;

    @GET
    @Path("/ping")
    public Response pingServer() {
        return Response.ok(
                new StandardResponseDto(
                        200,
                        "SmartCity360 REST API is running successfully!",
                        "GlassFish Server OK"
                )
        ).build();
    }

    @GET
    @Path("/db-check")
    public Response checkDatabaseConnection() {
        try {
            Object result = entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return Response.ok(
                    new StandardResponseDto(200, "Database connection is active and healthy!", result)
            ).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                    new StandardResponseDto(500,
                            "Database connection failed: " + e.getMessage(),
                            null
                    )
            ).build();
        }
    }
}
