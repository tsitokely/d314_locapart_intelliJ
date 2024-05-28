/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.Apartment;
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
public class ApartmentDAO {
    
    public static Apartment[] getAllApartments(){
        List<Apartment> apartments=new ArrayList<Apartment>();
        try(ResultSet rs=SQLite.getConnection().query(
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
        )){
            AddApartmentsFromQuery(apartments, rs);
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return apartments.toArray(Apartment[]::new);
    }

    public static Apartment[] getApartmentDetails(int apartmentID){
        List<Apartment> apartments=new ArrayList<Apartment>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT " +
                        "	 a.apartmentId\n" +
                        "	,c.cityID\n" +
                        "	,a.apartmentName\n" +
                        "	,a.apartmentDesc\n" +
                        "	,a.apartmentPrice\n" +
                        "	,a.apartmentAdress\n" +
                        "FROM Apartments a\n" +
                            "JOIN Cities c ON a.cityId = c.cityId\n" +
                        "WHERE a.apartmentId = '" + apartmentID + "'\n" +
                        "ORDER BY 1"
        )){
            AddApartmentsFromQuery(apartments, rs);
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return apartments.toArray(Apartment[]::new);
    }

    public static Apartment[] getAllApartmentsFromCity(String cityIDParam){
        List<Apartment> apartments=new ArrayList<Apartment>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT " +
                        "	 apartmentId\n" +
                        "	,c.cityId\n" +
                        "	,apartmentName\n" +
                        "	,apartmentDesc\n" +
                        "	,apartmentPrice\n" +
                        "	,apartmentAdress\n" +
                        "FROM Apartments a\n" +
                            "JOIN Cities c ON a.cityId = c.cityId\n" +
                        "WHERE c.cityID = '" + cityIDParam + "'\n" +
                        "ORDER BY 1"
        )){
            AddApartmentsFromQuery(apartments, rs);
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return apartments.toArray(Apartment[]::new);
    }

    public static Apartment[] getAllApartmentsFromCityAndPeriod(String cityIDParam,int yearStart,int yearEnd, int weekStart,int weekEnd){
        List<Apartment> apartments=new ArrayList<Apartment>();
        try(ResultSet rs=SQLite.getConnection().query(
                "SELECT " +
                        "	 a.apartmentId\n" +
                        "	,c.cityId\n" +
                        "	,a.apartmentName\n" +
                        "	,a.apartmentDesc\n" +
                        "	,a.apartmentPrice\n" +
                        "	,a.apartmentAdress\n" +
                        "FROM Apartments a\n" +
                            "INNER JOIN Cities c ON a.cityId = c.cityId\n" +
                            "LEFT JOIN Reservations r ON a.apartmentID = r.apartmentID\n" +
                        "WHERE NOT EXISTS (\n" +
                        "   SELECT 1\n" +
                        "   FROM Reservations r\n" +
                        "   WHERE r.apartmentID = a.apartmentID \n" +
                        "   AND (r.reservationDateYear > " + yearStart + " OR \n" +
                        "       (r.reservationDateYear =  " + yearStart + "  AND r.reservationDateNoSem >= " + weekStart + ")\n" +
                        "   )\n" +
                        "   AND (r.reservationDateYear < " + yearEnd + " OR \n" +
                        "       (r.reservationDateYear = " + yearEnd + " AND r.reservationDateNoSem <= " + weekEnd + ")\n" +
                        "   )\n" +
                        ")\n" +
                            "AND c.cityID = '" + cityIDParam + "'\n" +
                        "ORDER BY 1"
        )){
            AddApartmentsFromQuery(apartments, rs);
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentDAO.class.getName()).log(Level.SEVERE,null, ex);
        }
        return apartments.toArray(Apartment[]::new);
    }

    private static void AddApartmentsFromQuery(List<Apartment> apartments, ResultSet rs) throws SQLException {
        while(rs.next()){
            int apartmentId=rs.getInt("apartmentId");
            String CityID=rs.getString("CityID");
            String apartmentName=rs.getString("apartmentName");
            String apartmentDesc=rs.getString("apartmentDesc");
            float apartmentPrice=rs.getFloat("apartmentPrice");
            String apartmentAddress=rs.getString("apartmentAdress");
            apartments.add(new Apartment(apartmentId, CityID, apartmentName, apartmentDesc, apartmentPrice, apartmentAddress));
        }
    }

}
