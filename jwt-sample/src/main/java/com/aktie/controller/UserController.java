package com.aktie.controller;

import java.util.UUID;

import org.eclipse.microprofile.jwt.Claim;

import com.aktie.dto.UserDTO;
import com.aktie.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("api/v1/user")
public class UserController {

    @Inject
    UserService service;

    @Inject
    @Claim("uuid")
    String userId;

    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public Response adminResource() {
        var user = service.findById(UUID.fromString(userId));

        return Response.ok(user).build();
    }

    @GET
    @RolesAllowed({ "ADMIN", "USER" })
    public Response userResource() {
        var user = service.findById(UUID.fromString(userId));

        return Response.ok(user).build();
    }

    @GET
    @Path("/pub")
    public String publicResource() {
        return "Just a PUBLIC resource";
    }

    @POST
    public Response create(UserDTO dto) {
        service.create(dto);

        return Response.status(201).build();
    }

}
