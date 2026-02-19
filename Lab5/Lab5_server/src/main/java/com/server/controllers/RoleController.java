package com.server.controllers;
import com.server.network.Response;
import com.server.serializer.Serializer;
import com.server.services.RoleService;

public class RoleController {
    private static RoleService roleService = new RoleService();

    public Response getAllRoles() {
        return new Response(true, "Roles: ", Serializer.serialize(roleService.getAll()));
    }
}