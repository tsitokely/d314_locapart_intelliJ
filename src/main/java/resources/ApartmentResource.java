package resources;

import DAO.ApartmentDAO;
import helper.SQLite;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tsito
 */
@Path("Apartment")
public class ApartmentResource {
    
    @GET
    @Path("/")
    @Produces("application/json")
    public Response ping(){
        return Response
                .ok(ApartmentDAO
                        .getAllApartments())
                .status(200)
                .build();
    }

    @GET
    @Path("/{cityId}")
    @Produces("application/json")
    public Response ApartmentsInCity(@PathParam("cityId") String cityId){
        return Response
                .ok(ApartmentDAO
                        .getAllApartmentsFromCity(cityId))
                .status(200)
                .build();
    }

    @GET
    @Path("/{cityId}/{yearStart}-{weekStart}/{yearEnd}-{weekEnd}")
    @Produces("application/json")
    public Response ApartmentsInCityAndPeriod(
            @PathParam("cityId") String cityId,
            @PathParam("yearStart") int yearStart,
            @PathParam("weekStart") int weekStart,
            @PathParam("yearEnd") int yearEnd,
            @PathParam("weekEnd") int weekEnd){
        Logger.getLogger(SQLite.class.getName()).log(Level.INFO,cityId);
        Logger.getLogger(SQLite.class.getName()).log(Level.INFO,String.valueOf(yearStart));
        Logger.getLogger(SQLite.class.getName()).log(Level.INFO,String.valueOf(weekStart));
        Logger.getLogger(SQLite.class.getName()).log(Level.INFO,String.valueOf(yearEnd));
        Logger.getLogger(SQLite.class.getName()).log(Level.INFO,String.valueOf(weekEnd));
        return Response
                .ok(ApartmentDAO
                        .getAllApartmentsFromCityAndPeriod(cityId, yearStart, yearEnd, weekStart, weekEnd))
                .status(200)
                .build();
    }

}
