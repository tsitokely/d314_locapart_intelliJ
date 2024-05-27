package resources;

import DAO.ApartmentDAO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 *
 * @author 
 */
@Path("Apartment")
public class ApartmentResource {
    
    @GET
    @Path("/")
    @Produces("application/json")
    public Response ping(@Context jakarta.ws.rs.core.UriInfo uri){
        MultivaluedMap<String, String> p = uri.getQueryParameters();
        return Response
                .ok(ApartmentDAO
                        .getAllApartments())
                .status(200)
                .build();
    }

}
