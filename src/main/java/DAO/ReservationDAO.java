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
        // Faire un loop sur les données d'entrée
        for (ReservationDTO r : reservationsList){
            i++ ;

            // Vérifier si la donnée de réservation en cours de loop 'r' est une donnée de réservation DTO
            String jsonInput;
            // Convertir la donnée de réservation en cours de loop 'r' vers JSON
            // Retourner une erreur si non convertible
            try {
                jsonInput = objectMapper.writeValueAsString(r);
            } catch (Exception e) {
                console.log(Level.SEVERE, "Error converting reservation to JSON", e);
                return false;
            }
            console.log(Level.INFO, "input: {0}", jsonInput);

            // Vérifier la donnée converti en cours de loop 'r' vers JSON si conforme à une donnée de réservation DTO
            // Retourner une erreur si non conforme
            if (!compareJsonWithTemplate(jsonInput)) {
                console.log(Level.SEVERE, "Invalid input format for: {0}", jsonInput);
                return false;
            }

            // Si la donnée en cours de loop 'r' est conforme, procéder aux opérations au niveau database

            try {
                // Vérifier qu'il n'y a pas déjà une réservation similaire dans la BD
                // Retourner une erreur si la réservation existe déjà
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
                    // Vérifier que l'appartement est disponible sur la période séléctionné
                    // Retourner une erreur si l'appartement n'est pas disponible
                    ResultSet rs1 = SQLite.getConnection().query(
                            "SELECT * \n" +
                                    "FROM Apartments a\n" +
                                    "INNER JOIN Cities c ON a.cityId = c.cityId\n" +
                                    "LEFT JOIN Reservations r ON a.apartmentID = r.apartmentID\n" +
                                    "WHERE EXISTS (\n" +
                                    "   SELECT 1\n" +
                                    "   FROM Reservations r\n" +
                                    "   WHERE r.apartmentID = a.apartmentID \n" +
                                    "       AND (r.reservationDateYear =  "+ r.getReservationDateYear() +" AND r.reservationDateNoSem = "+r.getReservationDateNoSem() +")\n" +
                                    ")\n" +
                                    "AND a.apartmentID =" + r.getApartmentID() + "\n" +
                                    "ORDER BY 1"
                    );
                    if (rs1.next()) {
                        console.log(Level.INFO, "Apartment not available on this period: Year: {0}, Week: {1}.",new Object[]{r.getReservationDateYear(), r.getReservationDateNoSem() });
                        return false;
                    } else {
                        // Si toutes les vérifications sont OK, faire l'insertion dans la database
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
                }
            } catch (SQLException ex) {
                console.log(Level.SEVERE,null, ex);
                return false;
            }
        }
        console.log(Level.INFO, "Nombre de données d'entrée: {0}", i);
        return true;
    }

    public static int EditReservation(int reservationId, Reservation newReservation) {
        try {
            // Vérifier qu'il n'y a pas de réservation existantes
            ResultSet rs = SQLite.getConnection().query(
                    "SELECT * " +
                            "FROM Reservations " +
                            "WHERE reservationName = '" + newReservation.getReservationName() + "' " +
                            " AND reservationDateYear = " + newReservation.getReservationDateYear() +
                            " AND reservationDateNoSem = " + newReservation.getReservationDateNoSem() +
                            " AND apartmentID = " + newReservation.getApartmentID() +
                            " ;"
            );
            if (rs.next()) {
                console.log(Level.INFO, "Cannot update: similar reservation already exists.");
                return -1;
            } else {
                try {
                    // Vérifier que l'appartement est disponible sur la période
                    ResultSet rs1 = SQLite.getConnection().query(
                            "SELECT * \n" +
                                    "FROM Apartments a\n" +
                                    "INNER JOIN Cities c ON a.cityId = c.cityId\n" +
                                    "LEFT JOIN Reservations r ON a.apartmentID = r.apartmentID\n" +
                                    "WHERE EXISTS (\n" +
                                    "   SELECT 1\n" +
                                    "   FROM Reservations r\n" +
                                    "   WHERE r.apartmentID = a.apartmentID \n" +
                                    "       AND (r.reservationDateYear =  "+ newReservation.getReservationDateYear() +" AND r.reservationDateNoSem = "+newReservation.getReservationDateNoSem() +")\n" +
                                    ")\n" +
                                    "AND a.apartmentID =" + newReservation.getApartmentID() + "\n" +
                                    "ORDER BY 1"
                    );
                    if (rs1.next()) {
                        console.log(Level.INFO, "Apartment not available on this period.");
                        return -2;
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
                                        "reservationID = " + reservationId + " " +
                                        ";"
                        );
                        Logger.getLogger(ReservationDAO.class.getName()).log(Level.INFO, "Reservation edited: {0}", newReservation);
                        return 1;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public static boolean DeleteReservation(int reservationId) {
        try {
            SQLite.getConnection().query(
                    "DELETE " +
                        "FROM Reservations " +
                            "WHERE reservationID = " + reservationId  + " " +
                            " ;"
            );
            console.log(Level.INFO, "Reservation deleted: {0}", reservationId);
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
