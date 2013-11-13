/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssmanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Wise-SW
 */
public class RSSReader {
    private String rssURL;
    private HttpClient httpClient;
    private HttpGet httpGet;
    private ResponseHandler<String> responseHandler;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs ;
    public RSSReader(String rssURL) {
        this.rssURL = rssURL;
        httpClient = new DefaultHttpClient();
        httpGet = new HttpGet(rssURL);
        responseHandler = new BasicResponseHandler();
    }
    
    
    public int loadRSS(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:RSSManager.db");
            stmt = conn.createStatement();
            String query = "";
            Vector<RSSItem> items = new Vector<>();
            String responseBody = httpClient.execute(httpGet, responseHandler);
            String title = "";
            String link = "";
            String item = "";
            String startTitle = "<title>";
            String startLink = "<link>";
            String startItem = "<item>";
            String itemTitle = "";
            String itemLink = "";
            String desc = "";
            String creator = "";
            String date = "";
            String startDesc = "<description>";
            String startCreator = "<dc:creator>";
            String startDate = "<pubDate>";
            int index1 = 0,index2 = 0;
            int start = responseBody.indexOf(startTitle,0);
            int end = responseBody.indexOf("</title>",start);
            if(start >= 0 && end > start){
                title = responseBody.substring(start+startTitle.length(), end).replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
            }

            start = responseBody.indexOf(startLink,0);
            end = responseBody.indexOf("</link>", start);
            if(start >= 0 && end > start){
                link = responseBody.substring(start+startLink.length(),end).replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
            }

            
            start = end = 0;
            query = "SELECT * FROM RSS WHERE link = '"+link+"'";
            rs = stmt.executeQuery(query);
            int lastID = 0;
            if(!rs.next())
            {
                query = "INSERT INTO RSS(title,link,rssLink)"
                    + "VALUES('"+title+"','"+link+"','"+rssURL+"');";
                stmt.executeUpdate(query);
                query = "select last_insert_rowid() FROM RSS;";
                rs = stmt.executeQuery(query);
                lastID = rs.getInt("last_insert_rowid()");
            
                while(true){
                    start = responseBody.indexOf(startItem,start+1);
                    end = responseBody.indexOf("</item>", start);
                    if(start >= 0 && end > start)
                        item = responseBody.substring(start, end);
                    else break;
                    index1 = item.indexOf(startTitle, 0);
                    index2 = item.indexOf("</title>",index1);
                    if(index1 >= 0 && index2 > index1){
                        itemTitle = item.substring(index1+startTitle.length(), index2).replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
                    }else break;

                    index1 = item.indexOf(startLink, 0);
                    index2 = item.indexOf("</link>",index1);
                    if(index1 >= 0 && index2 > index1){
                        itemLink = item.substring(index1+startLink.length(), index2).replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
                    }else break;

                    index1 = item.indexOf(startDesc, 0);
                    index2 = item.indexOf("</description>",index1);
                    if(index1 >= 0 && index2 > index1){
                        desc = item.substring(index1+startDesc.length(), index2).replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
                    }else break;

                    index1 = item.indexOf(startCreator, 0);
                    index2 = item.indexOf("</dc:creator>",index1);
                    if(index1 >= 0 && index2 > index1){
                        creator = item.substring(index1+startCreator.length(), index2).replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
                    }

                    index1 = item.indexOf(startDate,0);
                    index2 = item.indexOf("</pubDate>", index1);
                    if(start >= 0 && end > start){
                        date = item.substring(index1+startDate.length(),index2);
                    }else break;

                    RSSItem rSSItem = new RSSItem(itemTitle, itemLink, desc, creator,date);
                    rSSItem.setRead(true);
                    query = "INSERT INTO RSSItem(itemTitle,desc,creator,date,itemLink,isRead,rssID)"
                            + "VALUES('"+itemTitle.replace("\'","''")+"','"+desc.replace("\'","''")+"','"+creator+"','"+date+"','"+itemLink+"',"+0+",'"+lastID+"');";
                    stmt.executeUpdate(query);
                    items.add(rSSItem);
                }
                return lastID;
            }else{
                return rs.getInt("id");
            }
            
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RSSReader.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally{
            if(httpClient != null)
                httpClient.getConnectionManager().shutdown();
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
