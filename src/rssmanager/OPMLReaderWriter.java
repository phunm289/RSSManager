/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssmanager;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Wise-SW
 */
public class OPMLReaderWriter {
    private String text;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private HttpClient httpClient;
    private HttpGet httpGet;
    private ResponseHandler<String> responseHandler;
    
    public OPMLReaderWriter() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:RSSManager.db");
            stmt = conn.createStatement();
            httpClient = new DefaultHttpClient();
            responseHandler = new BasicResponseHandler();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OPMLReaderWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(OPMLReaderWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    

    public Vector<RSS> OpmlReader(String filePath){
        try {
            Vector<RSS> rsses = new Vector<>();
            FileInputStream fin = new FileInputStream(new File(filePath));
            DataInputStream din = new DataInputStream(fin);
            BufferedReader reader = new BufferedReader(new InputStreamReader(din));
            String line = "";
            while((line = reader.readLine()) != null){
                if(line.contains("<outline") && line.endsWith("/>")){
                    String title = line.substring(line.indexOf("\"", line.indexOf("text"))+1, line.indexOf("\"",line.indexOf("text")+10));
                    String newsUrl = line.substring(line.indexOf("\"", line.indexOf("htmlUrl"))+1, line.indexOf("\"",line.indexOf("htmlUrl")+10));
                    String rssUrl = line.substring(line.indexOf("\"", line.indexOf("xmlUrl"))+1, line.indexOf("\"",line.indexOf("xmlUrl")+10));;
                    String query = "INSERT INTO RSS(title,link,rssLink)"
                    + "VALUES('"+title+"','"+newsUrl+"','"+rssUrl+"');";
                    stmt.executeUpdate(query);
                    RSS rss = new RSS(title, newsUrl, rssUrl);
                    
                    query = "select last_insert_rowid() FROM RSS;";
                    rs = stmt.executeQuery(query);
                    int lastID = rs.getInt("last_insert_rowid()");
                    rss.setId(lastID);
                    rsses.add(rss);
                    httpGet = new HttpGet(rssUrl);
                    String responseBody = httpClient.execute(httpGet, responseHandler);
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
                        }else break;

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
                    }
                    
                }
            }
            return rsses;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            return null;
        } catch (IOException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        
    }
    
    public void OpmlWriter(Vector<RSS> rssCollection, Component parent) throws FileNotFoundException, UnsupportedEncodingException{
        String startOPML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                             + "<opml version=\"1.1\">\n"
                                + "   <head>\n"
                                + "      <title>RSSManager</title>\n"
                                + "      <dateModified>"+dateFormat.format(calendar.getTime())+"</dateModified>\n"
                                + "   </head>\n"
                                + "   <body>\n"
                                + "      <outline text = \""+text+"\">\n";
    
        String endOPML = "      </outline>\n"
                                + "   <body>\n"
                                + "</opml>";
        String content = "";
        for (RSS rss : rssCollection) {
            content += "         <outline text=\""+rss.getTitle()+"\" htmlUrl=\""+rss.getNewsLink()+"\" xmlUrl=\""+rss.getRssLink()+"\" />\n";
        }
        String opmlText = startOPML + content + endOPML;
        String path = System.getenv("USERPROFILE")+"\\Desktop";
        JFileChooser chooser = new JFileChooser(path);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("Outline Processor Markup Language (*.opml)", "opml"));
        int option = chooser.showSaveDialog(parent);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = new File(chooser.getSelectedFile().getAbsolutePath()+".opml");
            if(file.isFile()){
                int i = JOptionPane.showConfirmDialog(parent, "File is existed, do you want to replace?");
                if(i == JOptionPane.OK_OPTION){
                    PrintWriter writer = new PrintWriter(file, "UTF-8");
                    writer.write(opmlText);
                    writer.close();
                }
            }else{
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.write(opmlText);
                writer.close();
            }
        }
    }
    
    
}
