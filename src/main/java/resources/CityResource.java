package resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import DAO.CityDAO;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            return Response.ok(CityDAO.getSpecificCity(keyword)).build();
        }
        else if(p.containsKey("year")&&p.containsKey("nosem")){
            String yearString = (String)p.getFirst("year");
            int year;
            String nosemString = (String)p.getFirst("nosem");
            int nosem;
            try {
                year = Integer.parseInt(yearString);
                nosem = Integer.parseInt(nosemString);
                return Response.ok(CityDAO.searchForCityWithVacantApartment(year, nosem)).build();
            } 
            catch (NumberFormatException e) {
                Logger.getLogger(CityResource.class.getName()).log(Level.SEVERE, "Year: {0}, Week number: {1}", new Object[]{yearString, nosemString});
            }
        }
        return Response.ok(CityDAO.getAllCities()).build();
    }
}
