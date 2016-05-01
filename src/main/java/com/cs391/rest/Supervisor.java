package com.cs391.rest;

import com.cs391.service.UserManagement;
import com.google.gson.Gson;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;

@Path("supervisor")
public class Supervisor {
    
    @EJB
    private UserManagement userManagement;

    @GET
    @Path("all")
    public Response getAllSupervisors() {
        try {
            String json = new Gson().toJson(userManagement.getSupervisors());
            return Response.status(Response.Status.OK).entity(json).build();
        } catch (EJBException e) {
            JSONObject jsonObject = new JSONObject("{Error : Something went wrong}");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonObject.toString()).build();
        }
    }
        
    @GET
    @Path("{id}")
    public Response getProjectBySupervisorID(@PathParam("id") String id) {       
        try {
            if(userManagement.getSuperOfStudent(id) != null) {
                String json = new Gson().toJson(userManagement.getSuperOfStudent(id));
                return Response.status(Response.Status.OK).entity(json).build();
            } else {
                JSONObject jsonObject = new JSONObject("{Error : Student has no supervisor assigned}");
                return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
            }
        } catch (EJBException e) {
            JSONObject jsonObject = new JSONObject("{Error : Something went wrong}");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonObject.toString()).build();
        }
    }
}
