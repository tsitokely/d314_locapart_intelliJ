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
    private String cityID="<indéfini>";
    private String cityName="<indéfini>";
    
    public City(){}
    
    public City(String cityID, String cityName){
        this.cityID = cityID;
        this.cityName = cityName;
    }

    @XmlElement(name="cityID",required=true,nillable=true )
    public String getCityID() {
        return cityID;
    }

    @XmlElement(name="cityName",required=true,nillable=true)
    public String getCityName() {
        return cityName;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
