/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.Apartment;
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
public class ApartmentDAO {
    
    public static Apartment[] getAllAparments(){
        List<Apartment> apartments=new ArrayList<Apartment>();
        try{
            ResultSet rs=SQLite.getConnection().query(
                    "SELECT " +
                    "	 apartmentId\n" +
                    "	,c.cityId\n" +
                    "	,apartmentName\n" +
                    "	,apartmentDesc\n" +
                    "	,apartmentPrice\n" +                 
                    "	,apartmentAdress\n" +
                    "FROM Apartments a\n" +
                    "JOIN Cities c ON a.cityId = c.cityId\n" +
                    "ORDER BY 1"
            );           
            while(rs.next()){
                int apartmentId=rs.getInt("apartmentId");
                String CityID=rs.getString("CityID");
                String apartmentName=rs.getString("apartmentName");
                String apartmentDesc=rs.getString("apartmentDesc");
                float apartmentPrice=rs.getFloat("apartmentPrice");
                String apartmentAdress=rs.getString("apartmentAdress");
                apartments.add(new Apartment(apartmentId, CityID, apartmentName, apartmentDesc, apartmentPrice, apartmentAdress));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return apartments.toArray(Apartment[]::new);
    }
}
