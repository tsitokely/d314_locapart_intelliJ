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

/**
 *
 * @author 
 */
@Path("City")
public class CityResource {
    
    @GET
    @Produces("application/json")
    public Response ping(){
            return Response.ok(CityDAO.loadAll()).build();
    }
}
