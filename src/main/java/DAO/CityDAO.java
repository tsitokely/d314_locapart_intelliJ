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
    
    public static City[] loadAll(){
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
    
    public static City[] findCity(String keyword){
        List<City> citiesKey=new ArrayList<City>();
        try{
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "Query: {0}", keyword);
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT DISTINCT CityID, CityName FROM Cities r "
                    +"WHERE r.CityName like '%"+keyword+"%' OR r.CityID like '%"+keyword+"%';"
            );           
            while(rs.next()){
                String CityID=rs.getString("CityID");
                String CityName=rs.getString("CityName");
                citiesKey.add(new City(CityID,CityName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return citiesKey.toArray(City[]::new);
    }
    
        public static boolean create(City r){
        try{
            SQLite.getConnection().query(
                "INSERT INTO Cities(CityID,CityName) Values('"+r.getCityID()+"','"+r.getCityName()+"');"
            );
            Logger.getLogger(CityDAO.class.getName()).log(Level.INFO, "Query creation: {0}", r);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return false;
    }
}
