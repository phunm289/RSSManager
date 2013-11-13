/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wise-SW
 */
public class Database {

    public Database() {
    }
    
    public void createDatabase(){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:RSSManager.db");
            stmt = c.createStatement();
            String query = "CREATE TABLE RSS"
                                + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                + " title varchar(100) NOT NULL,"
                                + " link varchar(100) NOT NULL,"
                                + " rssLink varchar(100) NOT NULL);"
                            +"CREATE TABLE RSSItem"
                                + "(itemID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                                + " itemTitle varchar(50) NOT NULL,"
                                + " desc varchar(200) NOT NULL,"
                                + " creator varchar(20) NOT NULL,"
                                + " date varchar(20) NOT NULL,"
                                + " itemLink varchar(50) NOT NULL,"
                                + " isRead int NOT NULL,"
                                + " rssID int NOT NULL,"
                                + " FOREIGN KEY(rssID) REFERENCES RSS(id));";
            stmt.executeUpdate(query);
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
