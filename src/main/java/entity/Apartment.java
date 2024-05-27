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

@XmlRootElement(name="Apartment")
public class Apartment {
    private int apartmentID=0;
    private String cityID="<indefinite>";
    private String apartmentName="<indefinite>";
    private String apartmentDesc="<indefinite>";
    private float apartmentPrice = 0.0f;
    private String apartmentAddress ="<indefinite>";
    
    
    public Apartment(){}
    
    public Apartment(int apartmentID, String cityID, String apartmentName, String apartmentDesc,  float apartmentPrice,  String apartmentAddress ){
        this.apartmentID = apartmentID;
        this.cityID = cityID;
        this.apartmentName = apartmentName;
        this.apartmentDesc = apartmentDesc;
        this.apartmentPrice = apartmentPrice;
        this.apartmentAddress = apartmentAddress;
    }

    public int getApartmentID() {
        return apartmentID;
    }

    public String getCityID() {
        return cityID;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public String getApartmentDesc() {
        return apartmentDesc;
    }

    public float getApartmentPrice() {
        return apartmentPrice;
    }

    public String getApartmentAddress() {
        return apartmentAddress;
    }

    public void setApartmentID(int apartmentID) {
        this.apartmentID = apartmentID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public void setApartmentDesc(String apartmentDesc) {
        this.apartmentDesc = apartmentDesc;
    }

    public void setApartmentPrice(float apartmentPrice) {
        this.apartmentPrice = apartmentPrice;
    }

    public void setApartmentAddress(String apartmentAddress) {
        this.apartmentAddress = apartmentAddress;
    }
}
