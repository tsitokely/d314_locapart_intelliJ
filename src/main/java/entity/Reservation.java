/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tsito
 */

@XmlRootElement(name="Reservation")
public class Reservation {
    private int reservationID=0;
    private String reservationName="<indefinite>";
    private int reservationDateYear=0;
    private int reservationDateNoSem=0;
    private int apartmentID = 0;


    public Reservation(){}

    public Reservation( int reservationID, String reservationName, int reservationDateYear, int reservationDateNoSem, int apartmentID ){
        this.reservationID = reservationID;
        this.reservationName = reservationName;
        this.reservationDateYear = reservationDateYear;
        this.reservationDateNoSem = reservationDateNoSem;
        this.apartmentID = apartmentID;
    }

    public int getReservationID() {
        return reservationID;
    }

    public String getReservationName() {
        return reservationName;
    }

    public int getReservationDateYear() {
        return reservationDateYear;
    }

    public int getReservationDateNoSem() {
        return reservationDateNoSem;
    }

    public int getApartmentID() {
        return apartmentID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public void setReservationDateYear(int reservationDateYear) {
        this.reservationDateYear = reservationDateYear;
    }

    public void setReservationDateNoSem(int reservationDateNoSem) {
        this.reservationDateNoSem = reservationDateNoSem;
    }

    public void setApartmentID(int apartmentID) {
        this.apartmentID = apartmentID;
    }
}
