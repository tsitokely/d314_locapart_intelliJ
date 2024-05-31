/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author tsito
 */

public class ReservationDTO {
    private String reservationName;
    private int reservationDateYear;
    private int reservationDateNoSem;
    private int apartmentID;

    // Getters and Setters
    public String getReservationName() { return reservationName; }
    public void setReservationName(String reservationName) { this.reservationName = reservationName; }

    public int getReservationDateYear() { return reservationDateYear; }
    public void setReservationDateYear(int reservationDateYear) { this.reservationDateYear = reservationDateYear; }

    public int getReservationDateNoSem() { return reservationDateNoSem; }
    public void setReservationDateNoSem(int reservationDateNoSem) { this.reservationDateNoSem = reservationDateNoSem; }

    public int getApartmentID() { return apartmentID; }
    public void setApartmentID(int apartmentID) { this.apartmentID = apartmentID; }
    private Reservation convertToReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setReservationName(reservationDTO.getReservationName());
        reservation.setReservationDateYear(reservationDTO.getReservationDateYear());
        reservation.setReservationDateNoSem(reservationDTO.getReservationDateNoSem());
        reservation.setApartmentID(reservationDTO.getApartmentID());
        return reservation;
    }
}
