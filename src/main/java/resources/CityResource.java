package resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import DAO.CityDAO;
import entity.City;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 *
 * @author 
 */
@Path("City")
public class CityResource {
    
    @GET
    @Produces("application/json")
    public Response ping(@Context jakarta.ws.rs.core.UriInfo uri){
        MultivaluedMap p = uri.getQueryParameters();
        if(p.containsKey("q")){
            String keyword = (String)p.getFirst("q");
            return Response.ok(CityDAO.findCity(keyword)).build();
        }
        return Response.ok(CityDAO.loadAll()).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creer(City r){
        boolean result = CityDAO.create(r);
        if(result){
            return Response.ok().status(Response.Status.CREATED).build();
        } else{
            return Response.notModified().build();
        }
    }
}
