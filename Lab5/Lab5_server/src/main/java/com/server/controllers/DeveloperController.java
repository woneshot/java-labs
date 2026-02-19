package com.server.controllers;
import com.server.network.Response;
import com.server.serializer.Serializer;
import com.server.services.DeveloperService;

public class DeveloperController {
    private static DeveloperService developerService = new DeveloperService();

    public Response getAllDevelopers() {
        return new Response(true, "Developers: ", Serializer.serialize(developerService.getAll()));
    }
}