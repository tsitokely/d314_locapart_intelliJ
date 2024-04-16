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
        List<City> liste=new ArrayList<City>();
        try{
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT CityID, CityName FROM Cities r ;"
            );           
            while(rs.next()){
                String CityID=rs.getString("CityID");
                String CityName=rs.getString("CityName");
                liste.add(new City(CityID,CityName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return liste.toArray(City[]::new);
    }
}
