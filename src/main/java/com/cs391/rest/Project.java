package com.cs391.rest;

import com.cs391.service.ProjectManagement;
import com.google.gson.Gson;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;

@Path("project")
public class Project {
    
    @EJB
    private ProjectManagement projectManagement;

    @GET
    @Path("all")
    public Response getAllProjects() {
        try {
            if (projectManagement.getProjects(null).size() > 1) {
                String json = new Gson().toJson(projectManagement.getProjects(null));
                return Response.status(Response.Status.OK).entity(json).build();
            } else {
                JSONObject jsonObject = new JSONObject("{Error : No projects are registered}");
                return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
            }
        } catch (EJBException e) {
            JSONObject jsonObject = new JSONObject("{Error : Something went wrong}");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonObject.toString()).build();
        }
    }
        
    @GET
    @Path("{id}")
    public Response getProjectBySupervisorID(@PathParam("id") String id) {       
        try {
            if (projectManagement.getProjectsBySupervisorId(id).size() > 1) {
                String json = new Gson().toJson(projectManagement.getProjectsBySupervisorId(id));
                return Response.status(Response.Status.OK).entity(json).build();
            } else {
                JSONObject jsonObject = new JSONObject("{Error : No projects found for supervisor}");
                return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
            }
        } catch (EJBException e) {
            JSONObject jsonObject = new JSONObject("{Error : Something went wrong}");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonObject.toString()).build();
        }
    }
}
