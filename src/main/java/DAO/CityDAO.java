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
    public static City loadAll(){
        City r=null;
        try{
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT CityID, CityName FROM Cities r ;"
            );           
            if(rs.next()){
                String CityID=rs.getString("CityID");
                String CityName=rs.getString("CityName");
                r=new City(CityID,CityName);
            } else throw new NotFoundException();
        } catch (SQLException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE,"Loading error", ex);
        }
        return r;
    }
}
