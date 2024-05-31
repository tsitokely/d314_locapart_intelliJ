/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.Reservation;
import entity.ReservationDTO;
import helper.SQLite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 *
 * @author tsito
 */
public class ReservationDAO {
    private static final Logger console = Logger.getLogger(ReservationDAO.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEMPLATE_JSON_CREATE = "{\"reservationName\":\"\",\"reservationDateYear\":0,\"reservationDateNoSem\":0,\"apartmentID\":0}";
    
    public static Reservation[] getAllReservations(){
        List<Reservation> reservations=new ArrayList<Reservation>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT " +
                        " r.reservationID\n" +
                        ",a.apartmentID\n" +
                        ",r.reservationName\n" +
                        ",r.reservationDateYear\n" +
                        ",r.reservationDateNoSem\n" +
                    "FROM Reservations r\n" +
                    "JOIN Apartments a ON r.apartmentID = a.apartmentID\n" +
                    "ORDER BY 1"
        )){
            AddReservationsFromQuery(reservations, rs);
        } catch (SQLException ex) {
            console.log(Level.SEVERE,null, ex);
        }
        return reservations.toArray(Reservation[]::new);
    }

    public static boolean InsertNewReservation(Object input){
        if (input instanceof ReservationDTO) {
            console.log(Level.INFO, "Single JSON");
            return InsertNewReservations(List.of((ReservationDTO) input));
        } else if (input instanceof List<?>){
            console.log(Level.INFO, "Array of JSON");
            List<?> inputList = (List<?>) input;
            for (Object obj : inputList) {
                if (!(obj instanceof ReservationDTO)) {
                    console.log(Level.SEVERE, "Invalid input format");
                    return false;
                }
            }
            return InsertNewReservations((List<ReservationDTO>) inputList);
        } else{
            console.log(Level.SEVERE, "Invalid input format");
            return false;
        }
    }

    public static boolean InsertNewReservations(List<ReservationDTO> reservationsList) {
        int i = 0;
        for (ReservationDTO r : reservationsList){
            i++ ;
            String jsonInput;
            try {
                jsonInput = objectMapper.writeValueAsString(r);
            } catch (Exception e) {
                console.log(Level.SEVERE, "Error converting reservation to JSON", e);
                return false;
            }

            console.log(Level.INFO, "input: {0}", jsonInput);
            if (!compareJsonWithTemplate(jsonInput)) {
                console.log(Level.SEVERE, "Invalid input format for: {0}", jsonInput);
                return false;
            }

            console.log(Level.INFO, "DB operation");
            try {
                // Vérifier qu'il n'y a pas déjà une réservation similaire dans la BD
                ResultSet rs = SQLite.getConnection().query(
                        "SELECT * " +
                                "FROM Reservations " +
                                "WHERE reservationName = '" + r.getReservationName() + "' " +
                                " AND reservationDateYear = " + r.getReservationDateYear() +
                                " AND reservationDateNoSem = " + r.getReservationDateNoSem() +
                                " AND apartmentID = " + r.getApartmentID() +
                                " ;"
                );
                if (rs.next()) {
                    console.log(Level.SEVERE, "Cannot create {0}: Similar reservation already exists.", r);
                    return false;
                } else {
                    SQLite.getConnection().query(
                            "INSERT INTO Reservations (reservationName,reservationDateYear,reservationDateNoSem, apartmentID)\n" +
                                    "Values('"+
                                    r.getReservationName() + "'," +
                                    r.getReservationDateYear() + "," +
                                    r.getReservationDateNoSem() + "," +
                                    r.getApartmentID() +
                                    ");"
                    );
                    console.log(Level.INFO, "Reservation created: {0}", r);;
                    continue;

                }
            } catch (SQLException ex) {
                console.log(Level.SEVERE,null, ex);
                return false;
            }
        }
        console.log(Level.INFO, "loop count: {0}", i);
        return true;
    }

    public static boolean EditReservation(Reservation oldReservation, Reservation newReservation) {
        try {
            // Vérifier qu'il n'y a pas de conflit avec les réservations existentes
            ResultSet rs = SQLite.getConnection().query(
                    "SELECT * " +
                            "FROM Reservations " +
                            "WHERE reservationName = '" + newReservation.getReservationName()  + "' " +
                                " AND reservationDateYear = " + newReservation.getReservationDateYear() +
                                " AND reservationDateNoSem = " + newReservation.getReservationDateNoSem() +
                                " AND apartmentID = " + newReservation.getApartmentID() +
                                " ;"
            );

            if (rs.next()) {
                console.log(Level.INFO, "Cannot update: similar reservation already exists.");
                return false;
            } else {
                // Update the reservation
                SQLite.getConnection().query(
                        "UPDATE Reservations \n" +
                            "SET \n" +
                                "reservationName = '" + newReservation.getReservationName() + "'," +
                                "reservationDateYear = " + newReservation.getReservationDateYear() + "," +
                                "reservationDateNoSem = " + newReservation.getReservationDateNoSem() + "," +
                                "apartmentID = " + newReservation.getApartmentID() + " \n" +
                                "WHERE \n" +
                                    "reservationName = '" + oldReservation.getReservationName() + "'," +
                                    "AND reservationDateYear = " + oldReservation.getReservationDateYear() + "," +
                                    "AND reservationDateNoSem = " + oldReservation.getReservationDateNoSem() + "," +
                                    "AND apartmentID = " + oldReservation.getApartmentID() + " \n" +
                                ";"
                );
                Logger.getLogger(ReservationDAO.class.getName()).log(Level.INFO, "Reservation edited: {0}", newReservation);
                return true;
            }
        } catch (SQLException ex) {
            console.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean DeleteReservation(Reservation r) {
        try {
            SQLite.getConnection().query(
                    "DELETE " +
                        "FROM Reservations " +
                            "WHERE reservationName = '" + r.getReservationName()  + "' " +
                            " AND reservationDateYear = " + r.getReservationDateYear() +
                            " AND reservationDateNoSem = " + r.getReservationDateNoSem() +
                            " AND apartmentID = " + r.getApartmentID() +
                            " ;"
            );
            console.log(Level.INFO, "Reservation deleted: {0}", r);
            return true;
        } catch (SQLException ex) {
            console.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static void AddReservationsFromQuery(List<Reservation> reservations, ResultSet rs) throws SQLException {
        while(rs.next()){
            int reservationID=rs.getInt("reservationID");
            String reservationName=rs.getString("reservationName");
            int reservationDateYear=rs.getInt("reservationDateYear");
            int reservationDateNoSem=rs.getInt("reservationDateNoSem");
            int apartmentID=rs.getInt("apartmentID");
            reservations.add(new Reservation(reservationID,reservationName,reservationDateYear,reservationDateNoSem,apartmentID));
        }
    }

    private static boolean compareJsonWithTemplate(String jsonInput) {
        try {
            JsonNode inputJson = objectMapper.readTree(jsonInput);
            JsonNode templateJson = objectMapper.readTree(TEMPLATE_JSON_CREATE);

            Set<String> inputKeys = objectMapper.convertValue(inputJson, Map.class).keySet();
            Set<String> templateKeys = objectMapper.convertValue(templateJson, Map.class).keySet();

            return inputKeys.equals(templateKeys);
        } catch (Exception e) {
            console.log(Level.SEVERE, "Error comparing JSON with template", e);
            return false;
        }
    }

}
