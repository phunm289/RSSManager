/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssmanager;

/**
 *
 * @author Wise-SW
 */
public class RSSItem {
    private String title, link, desc, creator, date;
    private int itemID;
    private boolean read = true;
    
    public RSSItem(String title, String link, String desc, String creator, String date) {
        this.title = title;
        this.link = link;
        this.desc = desc;
        this.creator = creator;
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
    
   

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public RSSItem() {
    }
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}
