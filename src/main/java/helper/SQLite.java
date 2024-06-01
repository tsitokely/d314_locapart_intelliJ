/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author tsito
 */
public class SQLite {
        static{
        try{
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE,null, ex);
        }
        System.out.println("Driver loaded");
    }
    
    private static SQLite _instance;
    private Connection connexion;
    
    private SQLite() throws SQLException{
        String chemin=this.getClass().getResource("/WsLocaPart.db").getPath();
        this.connexion=DriverManager.getConnection("jdbc:sqlite:"+chemin);
    }
    
    public static synchronized SQLite getConnection() throws SQLException{
        if (_instance == null){
            _instance=new SQLite();
            Logger.getLogger(SQLite.class.getName()).log(Level.INFO,"Helper SQLite created");
        }
        return _instance;
    }
    
    public ResultSet query(String sql) throws SQLException{
        Logger.getLogger(SQLite.class.getName()).log(Level.INFO,sql);
        Statement statement = connexion.createStatement();
        if(sql.startsWith("SELECT")){
            ResultSet executeQuery=statement.executeQuery(sql);
            return executeQuery;
        }
        statement.executeUpdate(sql);
        statement.close();
        return null;
    }
}