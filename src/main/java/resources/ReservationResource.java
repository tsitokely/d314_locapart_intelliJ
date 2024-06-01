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
            //Vérifier si la donnée d'entrée est un array of JSON ou un JSON unique
            if (jsonNode.isObject()){
                ReservationDTO reservationDTO = objectMapper.treeToValue(jsonNode, ReservationDTO.class);
                boolean success = ReservationDAO.InsertNewReservation(reservationDTO);
                // Lancer les opérations en DAO
                if (success) {
                    return Response.status(Response.Status.CREATED).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input format or database error").build();
                }
            } else if(jsonNode.isArray()) {
                List<ReservationDTO> reservations = new ArrayList<>();
                for (JsonNode node : jsonNode) {
                    ReservationDTO reservationDTO = objectMapper.treeToValue(node, ReservationDTO.class);
                    reservations.add(reservationDTO);
                }
                // Lancer les opérations en DAO
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
    @Path("/{reservationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response EditReservation(@PathParam("reservationId") int reservationId, Reservation newReservation){
        int result = ReservationDAO.EditReservation(reservationId, newReservation);
        if(result==1){
            return Response.ok().build();
        } else if (result==-1){
            return Response.status(Response.Status.CONFLICT).entity("Une réservation similaire existe - ne peut être mis à jour").build();
        } else if (result==-2){
            return Response.status(Response.Status.CONFLICT).entity("Appartement non disponible sur cette période").build();
        }  else{
            return Response.status(Response.Status.BAD_REQUEST).entity("Erreur dans la mise à jour de la réservation").build();
        }
    }

    @DELETE
    @Path("/{reservationId}")
    @Produces("application/json")
    public Response DeleteReservation(@PathParam("reservationId") int reservationId){
        boolean result = ReservationDAO.DeleteReservation(reservationId);
        if(result){
            return Response
                    .ok()
                    .entity("Reservation deleted.")
                    .build();
        } else{
            return Response.status(Response.Status.NOT_FOUND).entity("Reservation not found.").build();
        }
    }

}