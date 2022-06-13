package co.edu.unbosque.seviceF2.resources;

import co.edu.unbosque.serviciosf.model.UserApp;
import co.edu.unbosque.serviciosf.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    private UserService service;

    public UserResource(){
        service = new UserService();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registered(UserApp userApp){
        System.out.println(userApp.toString());
        return Response.ok()
                       .status(201)
                       .entity(service.addElement(userApp.getName(), userApp.getEmail(), userApp.getPassword(), userApp.getRole()))
                       .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserApp userApp){
        return Response.ok()
                       .entity(service.login(userApp.getName(), userApp.getPassword()))
                       .build();
    }

    @PUT
    @Path("/{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePrice(UserApp userApp){
        return null;
    }
}
