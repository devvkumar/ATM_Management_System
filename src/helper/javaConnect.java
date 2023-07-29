package helper;


import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dev Kumar
 */
public class javaConnect {
    
    public static Connection connectDb()
    {
        Connection conn = null;
        
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_dbms", "root", "1996");
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return conn;
    }
    
}
