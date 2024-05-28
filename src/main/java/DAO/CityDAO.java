/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.City;
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
public class CityDAO {
    
    public static City[] getAllCities(){
        List<City> cities=new ArrayList<City>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT " +
                        "cityID \n" +
                        ",CityName \n" +
                        ",cityLatitude \n" +
                        ",cityLongitude \n" +
                    "FROM Cities ;"
                )
            ){
            AddCitiesFromQuery(cities, rs);
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }

    public static City[] getCityInfo(String cityID){
        List<City> cities=new ArrayList<City>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT\n" +
                        "cityID\n" +
                        ",CityName\n" +
                        ",cityLatitude\n" +
                        ",cityLongitude \n" +
                    "FROM Cities \n" +
                    "WHERE CityID = '"+ cityID +"';"
        )
        ){
            AddCitiesFromQuery(cities, rs);
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }
    
    public static City[] getSpecificCity(String keyword){
        List<City> cities=new ArrayList<City>();
        try (ResultSet rs=SQLite.getConnection().query(
                "SELECT\n" +
                        "DISTINCT c.cityID\n" +
                        ",c.cityName\n" +
                        ",c.cityLatitude\n" +
                        ",c.cityLongitude\n" +
                    "FROM Cities c \n" +
                    "WHERE c.CityName like '%" + keyword + "%' \n" +
                        "OR c.CityID like '%" + keyword + "%' \n" +
                    "ORDER BY 1 ASC;"
        )){
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "Keyword to be searched: {0}", keyword);
            AddCitiesFromQuery(cities, rs);
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }
    
    public static City[] searchForCityWithVacantApartment(int yearStart,int yearEnd, int weekStart,int weekEnd){
        List<City> cities=new ArrayList<City>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT\n" +
                        "DISTINCT c.cityID\n" +
                        ",c.cityName\n" +
                        ",c.cityLatitude\n" +
                        ",c.cityLongitude\n" +
                    "FROM Cities c \n" +
                        "INNER JOIN Apartments a ON c.cityId = a.cityID \n" +
                        "LEFT JOIN Reservations r ON a.apartmentID = r.apartmentID \n" +
                    "WHERE NOT EXISTS ( \n" +
                        "SELECT 1 \n" +
                        "FROM Reservations rr \n" +
                        "WHERE rr.apartmentID = a.apartmentID \n" +
                            "AND (rr.reservationDateYear >" + yearStart + " \n" +
                                "OR (rr.reservationDateYear =" + yearStart + " AND rr.reservationDateNoSem >=" + weekStart + " )) \n" +
                            "AND (rr.reservationDateYear <" + yearEnd + " \n" +
                                "OR (rr.reservationDateYear =" + yearEnd + " AND rr.reservationDateNoSem <=" + weekEnd + " )) \n" +
                    ") ORDER BY 1 ASC;"
        )){
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "YearStart: {0}, WeekStart: {1}, YearStart: {2}, WeekStart: {3}", new Object[]{yearStart, weekStart, yearEnd,weekEnd });
            AddCitiesFromQuery(cities, rs);
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return cities.toArray(City[]::new);
    }



    // helper pour les requêtes
    private static void AddCitiesFromQuery(List<City> cities, ResultSet rs) throws SQLException {
        while(rs.next()){
            String CityID = rs.getString("CityID");
            String CityName = rs.getString("CityName");
            float cityLatitude = rs.getFloat("cityLatitude");
            float cityLongitude = rs.getFloat("cityLongitude");
            cities.add(new City(CityID,CityName, cityLatitude, cityLongitude));
        }
    }
}
