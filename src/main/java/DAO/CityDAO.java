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
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT CityID, CityName FROM Cities r ;"
        )){
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
        try (ResultSet rs=SQLite.getConnection().query(
                "SELECT DISTINCT CityID, CityName FROM Cities r "
                        +"WHERE r.CityName like '%"+keyword+"%' OR r.CityID like '%"+keyword+"%' ORDER BY 1 ASC;"
        )){
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "Keyword to be searched: {0}", keyword);
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
    
        public static City[] searchForCityWithVacantApartment(int yearStart,int yearEnd, int weekStart,int weekEnd){
        List<City> cities=new ArrayList<City>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT DISTINCT Cities.cityId, Cities.cityName"
                        + " FROM Cities"
                        + " INNER JOIN Apartments ON Cities.cityId = Apartments.cityID"
                        + " LEFT JOIN Reservations ON Apartments.apartmentID = Reservations.apartmentID"
                        + " WHERE NOT EXISTS ("
                        + "     SELECT 1"
                        + "     FROM Reservations"
                        + "     WHERE Reservations.apartmentID = Apartments.apartmentID AND"
                        + " 		  (Reservations.reservationDateYear >" + yearStart + " OR "
                        + "           	(Reservations.reservationDateYear =" + yearStart + " AND Reservations.reservationDateNoSem >=" + weekStart + " ))"
                        + "       AND (Reservations.reservationDateYear <" + yearEnd + " OR"
                        + "           	(Reservations.reservationDateYear =" + yearEnd + " AND Reservations.reservationDateNoSem <=" + weekEnd + " ))"
                        + " ) ORDER BY 1 ASC;"
        )){
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "YearStart: {0}, WeekStart: {1}, YearStart: {2}, WeekStart: {3}", new Object[]{yearStart, weekStart, yearEnd,weekEnd });
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
