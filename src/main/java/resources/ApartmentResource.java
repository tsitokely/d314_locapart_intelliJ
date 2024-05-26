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
public class ApartmentResource {
    
    @GET
    @Produces("application/json")
    public Response ping(@Context jakarta.ws.rs.core.UriInfo uri){
        MultivaluedMap p = uri.getQueryParameters();
        if(p.containsKey("q")){
            String keyword = (String)p.getFirst("q");
            return Response.ok(CityDAO.getSpecificCity(keyword)).build();
        }
        else if(p.containsKey("year")&&p.containsKey("week")){
            String yearString = (String)p.getFirst("year");
            int year;
            String weekString = (String)p.getFirst("week");
            int week;
            try {
                year = Integer.parseInt(yearString);
                week = Integer.parseInt(weekString);
                return Response.ok(CityDAO.searchForCityWithVacantApartment(year,year,week,week)).build();
            } 
            catch (NumberFormatException e) {
                Logger.getLogger(ApartmentResource.class.getName()).log(Level.SEVERE, "Year: {0}, Week number: {1}", new Object[]{yearString, weekString});
            }
        }
        else if(p.containsKey("yearStart")&&p.containsKey("yearEnd")&&p.containsKey("weekStart")&&p.containsKey("weekEnd")){
            String yearStartString = (String)p.getFirst("yearStart");
            String yearEndString = (String)p.getFirst("yearEnd");
            int yearStart;
            int yearEnd;
            String weekStartString = (String)p.getFirst("weekStart");
            String weekEndString = (String)p.getFirst("weekEnd");
            int weekStart;
            int weekEnd;
            try {
                yearStart = Integer.parseInt(yearStartString);
                yearEnd = Integer.parseInt(yearEndString);
                weekStart = Integer.parseInt(weekStartString);
                weekEnd = Integer.parseInt(weekEndString);
                return Response.ok(CityDAO.searchForCityWithVacantApartment(yearStart,yearEnd,weekStart,weekEnd)).build();
            } 
            catch (NumberFormatException e) {
                Logger.getLogger(ApartmentResource.class.getName()).log(Level.SEVERE, "YearStart: {0}, WeekStart: {1}, YearEnd: {2}, WeekEnd: {3}", new Object[]{yearStartString, weekStartString, yearEndString, weekEndString});
            }
        }
        return Response.ok(CityDAO.getAllCities()).build();
    }
}
