package resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.Context;
import DAO.CityDAO;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
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
    @Path("/")
    @Produces("application/json")
    public Response ping(@Context jakarta.ws.rs.core.UriInfo uri){
        MultivaluedMap p = uri.getQueryParameters();
        if(p.containsKey("q")){
            String keyword = (String)p.getFirst("q");
            return Response
                    .ok(CityDAO.getSpecificCity(keyword))
                    .status(200)
                    .build();
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
                return Response
                        .ok(CityDAO.searchForCityWithVacantApartment(yearStart,yearEnd,weekStart,weekEnd))
                        .status(200)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Credentials", "true")
                        .header("Access-Control-Allow-Headers",
                                "origin, content-type, accept, authorization")
                        .header("Access-Control-Allow-Methods",
                                "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                        .entity("")
                        .build();
            } 
            catch (NumberFormatException e) {
                Logger.getLogger(CityResource.class.getName()).log(Level.SEVERE, "YearStart: {0}, WeekStart: {1}, YearEnd: {2}, WeekEnd: {3}", new Object[]{yearStartString, weekStartString, yearEndString, weekEndString});
            }
        }
        return Response.ok(CityDAO.getAllCities()).build();
    }
    
    @GET
    @Path("/{year}/{week}")
    @Produces("application/json")
    public Response cityAvailableInSpecificWeekRange(@PathParam("year") int yearStart,
                                                     @DefaultValue("0") @QueryParam("yearEnd") int yearEnd, 
                                                     @PathParam("week") int weekStart,
                                                     @DefaultValue("0") @QueryParam("weekEnd") int weekEnd){
        // Il n'y a pas d'année 0, ni de semaine avec le numéro 0 dans notre application
        boolean isYearEndProvided = yearEnd > 0;
        boolean isWeekEndProvided = weekEnd > 0;
        
        // Cas 1: les deux paramètres ne sont pas fournis
        if(!isYearEndProvided && !isWeekEndProvided) {
            return Response
                   .ok(CityDAO.searchForCityWithVacantApartment(yearStart,yearStart,weekStart,weekStart))
                   .build();
        }
        
        // Cas 2: un des deux paramètres ne sont pas fournis
        if(isYearEndProvided != isWeekEndProvided) {
            String provided = (isYearEndProvided?"yearEnd":"weekEnd");
            String providedValue = (isYearEndProvided?String.valueOf(yearEnd):String.valueOf(weekEnd));
            return Response
                    .status(Status.BAD_REQUEST)
                    .entity("L'un des deux paramètres est manquant - veuillez fournir les deux paramètres ou aucun paramètre - vous avez donné le paramètre \""+ provided + "\" avec la valeur " + providedValue)
                    .build();
        }
        
        // Cas 3: les deux paramètres sont fournis      
        return Response
                .ok(CityDAO.searchForCityWithVacantApartment(yearStart,yearEnd,weekStart,weekEnd))
                .build();
    }
}
