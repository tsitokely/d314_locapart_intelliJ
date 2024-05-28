/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tsito
 */

@XmlRootElement(name="City")
public class City {
    private String cityID="<indefinite>";
    private String cityName="<indefinite>";
    private float cityLatitude=0.0f;
    private float cityLongitude=0.0f;
    
    public City(){}
    
    public City(String cityID, String cityName, float cityLatitude , float cityLongitude){
        this.cityID = cityID;
        this.cityName = cityName;
        this.cityLatitude = cityLatitude;
        this.cityLongitude = cityLongitude;
    }

    @XmlElement(name="cityID",required=true,nillable=true )
    public String getCityID() {
        return cityID;
    }

    @XmlElement(name="cityName",required=true,nillable=true)
    public String getCityName() {
        return cityName;
    }

    @XmlElement(name="cityLatitude",required=true,nillable=true)
    public float getCityLatitude() {
        return cityLatitude;
    }

    @XmlElement(name="cityLongitude",required=true,nillable=true)
    public float getCityLongitude() {
        return cityLongitude;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityLatitude(float cityLatitude) {
        this.cityLatitude = cityLatitude;
    }

    public void setCityLongitude(float cityLongitude) {
        this.cityLongitude = cityLongitude;
    }
}
