package com.aktie.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RequestScoped
@Path("api/v1/user")
public class UserController {

    @GET
    @Path("/admin")
    public String adminResource() {
        return "ADMIN resource";
    }

    @GET
    public String userResource() {
        return "USER resource";
    }

    @GET
    @Path("/pub")
    public String publicResource() {
        return "Just a PUBLIC resource";
    }

}
