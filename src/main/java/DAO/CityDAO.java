/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.City;
import helper.SQLite;
import jakarta.ws.rs.NotFoundException;
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
public class CityDAO {
    
    public static City[] getAllCities(){
        List<City> cities=new ArrayList<City>();
        try{
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT CityID, CityName FROM Cities r ;"
            );           
            while(rs.next()){
                String CityID=rs.getString("CityID");
                String CityName=rs.getString("CityName");
                cities.add(new City(CityID,CityName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }
    
    public static City[] getSpecificCity(String keyword){
        List<City> cities=new ArrayList<City>();
        try{
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "Keyword to be searched: {0}", keyword);
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT DISTINCT CityID, CityName FROM Cities r "
                    +"WHERE r.CityName like '%"+keyword+"%' OR r.CityID like '%"+keyword+"%' ORDER BY 1 ASC;"
            );           
            while(rs.next()){
                String CityID=rs.getString("CityID");
                String CityName=rs.getString("CityName");
                cities.add(new City(CityID,CityName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }
    
        public static City[] searchForCityWithVacantApartment(int year, int noSem){
        List<City> cities=new ArrayList<City>();
        try{
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "Year: {0}, Week number: {1}", new Object[]{year, noSem});
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT DISTINCT cityID,cityName"
                    +" FROM ("
                    +" 	SELECT a.CityID cityID"
                    +" 		,c.CityName cityName"
                    +" 		,a.apartmentID"
                    +" 	FROM Apartments a"
                    +" 	LEFT JOIN Cities c ON c.CityID = a.CityID"
                    +" 	LEFT JOIN Reservations r ON a.apartmentID = r.apartmentID"
                    +" 	EXCEPT"
                    +" 	SELECT a.CityID"
                    +" 		,c.CityName"
                    +" 		,a.apartmentID"
                    +" 	FROM Apartments a"
                    +" 	LEFT JOIN Cities c ON c.CityID = a.CityID"
                    +" 	LEFT JOIN Reservations r ON a.apartmentID = r.apartmentID"
                    +" 	WHERE r.reservationDateYear =" + year
                    +" 		AND r.reservationDateNoSem ="+noSem
                    +" 	)"
                    +" ORDER BY 1 ASC;"
            );           
            while(rs.next()){
                String CityID=rs.getString("CityID");
                String CityName=rs.getString("CityName");
                cities.add(new City(CityID,CityName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }  
}
