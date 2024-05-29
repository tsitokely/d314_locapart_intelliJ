package resources;

import DAO.ApartmentDAO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

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

    /*@GET
    @Path("/{cityId}")
    @Produces("application/json")
    public Response ApartmentsInCity(@PathParam("cityId") String cityId){
        return Response
                .ok(ApartmentDAO
                        .getAllApartmentsFromCity(cityId))
                .status(200)
                .build();
    }*/

    @GET
    @Path("/{apartmentId}")
    @Produces("application/json")
    public Response ApartmentDetails(@PathParam("apartmentId") int apartmentId){
        return Response
                .ok(ApartmentDAO
                        .getApartmentDetails(apartmentId))
                .status(200)
                .build();
    }

    @GET
    @Path("/{yearStart}-{weekStart}/{yearEnd}-{weekEnd}")
    @Produces("application/json")
    public Response ApartmentsInCityAndPeriod(
            @PathParam("yearStart") int yearStart,
            @PathParam("weekStart") int weekStart,
            @PathParam("yearEnd") int yearEnd,
            @PathParam("weekEnd") int weekEnd){
        return Response
                .ok(ApartmentDAO
                        .getAllApartmentsFromPeriod(yearStart, yearEnd, weekStart, weekEnd))
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
        return Response
                .ok(ApartmentDAO
                        .getAllApartmentsFromCityAndPeriod(cityId, yearStart, yearEnd, weekStart, weekEnd))
                .status(200)
                .build();
    }
}