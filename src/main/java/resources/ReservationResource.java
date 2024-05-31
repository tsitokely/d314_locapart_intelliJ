package resources;

import DAO.ReservationDAO;
import entity.Reservation;
import entity.ReservationDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tsito
 */
@Path("Reservation")
public class ReservationResource {
    private static final Logger console = Logger.getLogger(ReservationResource.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
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
    public Response NewReservations(String inputJson){
        try {
            JsonNode jsonNode = objectMapper.readTree(inputJson);
            if (jsonNode.isObject()){
                ReservationDTO reservationDTO = objectMapper.treeToValue(jsonNode, ReservationDTO.class);
                boolean success = ReservationDAO.InsertNewReservation(reservationDTO);
                if (success) {
                    return Response.status(Response.Status.CREATED).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input format or database error").build();
                }
            } else if(jsonNode.isArray()) {
                List<ReservationDTO> reservations = new ArrayList<>();
                for (JsonNode node : jsonNode) {
                    console.log(Level.INFO,"your json is {0}",node);
                    ReservationDTO reservationDTO = objectMapper.treeToValue(node, ReservationDTO.class);
                    reservations.add(reservationDTO);
                }
                boolean success = ReservationDAO.InsertNewReservation(reservations);
                if (success) {
                    return Response.status(Response.Status.CREATED).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input format or database error").build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input format").build();
            }
        } catch (Exception e){
            console.log(Level.SEVERE, "JSON syntax error", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
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