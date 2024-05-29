/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.Apartment;
import entity.Reservation;
import helper.SQLite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tsito
 */
public class ReservationDAO {
    
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
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return reservations.toArray(Reservation[]::new);
    }

    public static boolean InsertNewReservation(Reservation r){
        try{
            // Vérifier qu'il n'y a pas déjà une réservation similaire dans la BD
            ResultSet rs = SQLite.getConnection().query(
                    "SELECT * " +
                        "FROM Reservations " +
                        "WHERE reservationName = " + r.getReservationName() +
                        " AND reservationDateYear = " + r.getReservationDateYear() +
                        " AND reservationDateNoSem = " + r.getReservationDateNoSem() +
                        " AND apartmentID = " + r.getApartmentID() +
                        " ;"
            );
            if (rs.next()) {
                Logger.getLogger(ReservationDAO.class.getName()).log(Level.INFO, "Cannot create: Similar reservation already exists.");
                return false;
            } else {
                SQLite.getConnection().query(
                        "INSERT INTO Reservations (reservationName,reservationDateYear,reservationDateNoSem, apartmentID)\n" +
                                "Values('"+
                                r.getReservationName() + "','" +
                                r.getReservationDateYear() + "','" +
                                r.getReservationDateNoSem() + "','" +
                                r.getApartmentID() +
                                "');"
                );
                Logger.getLogger(ReservationDAO.class.getName()).log(Level.INFO, "Reservation created: {0}", r);
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return false;
    }

    public static boolean EditReservation(Reservation oldReservation, Reservation newReservation) {
        try {
            // Vérifier qu'il n'y a pas de conflit avec les réservations existentes
            ResultSet rs = SQLite.getConnection().query(
                    "SELECT * " +
                            "FROM Reservations " +
                            "WHERE reservationName = " + newReservation.getReservationName() +
                                " AND reservationDateYear = " + newReservation.getReservationDateYear() +
                                " AND reservationDateNoSem = " + newReservation.getReservationDateNoSem() +
                                " AND apartmentID = " + newReservation.getApartmentID() +
                                " ;"
            );

            if (rs.next()) {
                Logger.getLogger(ReservationDAO.class.getName()).log(Level.INFO, "Cannot update: similar reservation already exists.");
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
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean DeleteReservation(Reservation r) {
        try {
            SQLite.getConnection().query(
                    "DELETE " +
                        "FROM Reservations " +
                            "WHERE reservationName = " + r.getReservationName() +
                            " AND reservationDateYear = " + r.getReservationDateYear() +
                            " AND reservationDateNoSem = " + r.getReservationDateNoSem() +
                            " AND apartmentID = " + r.getApartmentID() +
                            " ;"
            );
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.INFO, "Reservation deleted: {0}", r);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
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

}
