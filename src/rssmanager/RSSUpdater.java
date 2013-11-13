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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Wise-SW
 */
public class RSSUpdater {
    
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private HttpClient httpClient;
    private HttpGet httpGet;
    private ResponseHandler<String> responseHandler;
    private String rssURL;
    private Vector<RSSItem> feedNews = new Vector<>();
    public RSSUpdater(String rssURL) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:RSSManager.db");
            stmt = conn.createStatement();
            this.rssURL = rssURL;
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet(rssURL);
            responseHandler = new BasicResponseHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RSSUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public Vector<RSSItem> getFeedNews() {
        return feedNews;
    }

    public void setFeedNews(Vector<RSSItem> feedNews) {
        this.feedNews = feedNews;
    }
    
    public int Updater(){
        try {
            int news = 0;
            
            String responseBody = httpClient.execute(httpGet, responseHandler);
            Vector<RSSItem> itemsNew = new Vector<>();
            Vector<RSSItem> itemsOld = new Vector<>();
            int rssID = 0;
            String item = "";
            String itemTitle = "";
            String itemLink = "";
            String desc = "";
            String creator = "";
            String date = "";
            String startTitle = "<title>";
            String startLink = "<link>";
            String startItem = "<item>";
            String startDesc = "<description>";
            String startCreator = "<dc:creator>";
            String startDate = "<pubDate>";
            int index1 = 0,index2 = 0;
            int start = responseBody.indexOf(startTitle,0);
            int end = responseBody.indexOf("</title>",start);
            start = end = 0;
            while(true){
                //<editor-fold defaultstate="collapsed" desc="add item to items new">
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
                }
                
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
                
                RSSItem newItem = new RSSItem(itemTitle, itemLink, desc, creator, date);
                newItem.setRead(false);
                itemsNew.add(newItem);
                
                //</editor-fold>
            }
            rs = stmt.executeQuery("SELECT itemTitle,itemLink,desc,creator,date,id,isRead FROM RSSItem AS A JOIN RSS AS B ON A.rssID = B.id WHERE B.rssLink = '"+rssURL+"' ORDER BY itemID ASC");
            while(rs.next()){
                //<editor-fold defaultstate="collapsed" desc="add item to items old">
                itemTitle = rs.getString("itemTitle");
                itemLink = rs.getString("itemLink");
                desc = rs.getString("desc");
                creator = rs.getString("creator");
                date = rs.getString("date");
                rssID = rs.getInt("id");
                int read = rs.getInt("isRead");
                RSSItem itemOld = new RSSItem(itemTitle, itemLink, desc, creator, date);
                if(read == 1)
                    itemOld.setRead(true);
                else
                    itemOld.setRead(false);
                itemsOld.add(itemOld);
                
                //</editor-fold>
            }
            
            if(itemsOld.get(0).getLink().equals(itemsNew.get(0).getLink())){
                news = 0;
            }else{
                //update new here
                int count = 0;
                while(true){
                    if(itemsOld.get(0).getLink().equals(itemsNew.get(count).getLink())){
                        news = count;
                        break;
                    }else{
                        count++;
                    }
                }
                //insert count rssitem new vao vector temp, 
                Vector<RSSItem> itemTemp = new Vector<>();
                for (int i = 0; i < count; i++) {
                    itemTemp.add(itemsNew.get(i));
                    feedNews.add(itemsNew.get(i));
                }
                //sau do insert toan bo rssitem co rssid = rssid vao vector temp, 
                for (int i = 0; i < itemsOld.size(); i++) {
                    itemTemp.add(itemsOld.get(i));
                }
                //xoa toan bo rssitem co rss id = rssid trong rssitem, 
                stmt.execute("DELETE FROM RSSItem WHERE rssID = '"+rssID+"'");
                //insert toan bo item trong vector temp vao rssitem
                int isRead = 0;
                for (int i = 0; i < itemTemp.size(); i++) {
                    if(itemTemp.get(i).isRead()){
                        isRead = 1;
                    }else if(!itemTemp.get(i).isRead())
                        isRead = 0;
                    String query = "INSERT INTO RSSItem(itemTitle,desc,creator,date,itemLink,isRead,rssID)"
                            + "VALUES('"+itemTemp.get(i).getTitle().replace("\'","''")+"','"+itemTemp.get(i).getDesc().replace("\'","''")+"','"+itemTemp.get(i).getCreator()+"','"+itemTemp.get(i).getDate()+"','"+itemTemp.get(i).getLink()+"',"+isRead+",'"+rssID+"');";
                    stmt.executeUpdate(query);
                }
                
                //xoa vector temp
                itemTemp = null;
            }
            
            return news;
        } catch (IOException | SQLException ex) {
            return 0;
        } finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RSSUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(httpClient != null){
                httpClient.getConnectionManager().shutdown();
            }
        }
    }
    
}
