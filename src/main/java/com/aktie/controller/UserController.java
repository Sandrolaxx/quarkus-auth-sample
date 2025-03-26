package com.aktie.controller;

import com.aktie.dto.UserDTO;
import com.aktie.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("api/v1/user")
public class UserController {

    @Inject
    UserService service;
    
    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public String adminResource() {
        return "Just a ADMIN resource";
    }

    @GET
    @RolesAllowed({ "ADMIN", "USER" })
    public String userResource() {
        return "Just a USER resource";
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
