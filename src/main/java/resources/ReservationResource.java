package resources;

import DAO.ReservationDAO;
import entity.Reservation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author Tsito
 */
@Path("Reservation")
public class ReservationResource {
    
    @GET
    @Path("/")
    @Produces("application/json")
    public Response ping(){
        return Response
                .ok(ReservationDAO
                        .getAllReservations())
                .status(200)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response NewReservation(Reservation r){
        boolean result = ReservationDAO.InsertNewReservation(r);
        if(result){
            return Response.status(Response.Status.CREATED).build();
        } else{
            return Response.status(Response.Status.CONFLICT).entity("Similar reservation already exists.").build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response EditReservation(Reservation oldReservation, Reservation newReservation){
        boolean result = ReservationDAO.EditReservation(oldReservation, newReservation);
        if(result){
            return Response.ok().build();
        } else{
            return Response.status(Response.Status.CONFLICT).entity("Similar reservation already exists. Cannot edit.").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DeleteReservation(Reservation r){
        boolean result = ReservationDAO.DeleteReservation(r);
        if(result){
            return Response.ok().build();
        } else{
            return Response.status(Response.Status.NOT_FOUND).entity("Reservation not found.").build();
        }
    }

}