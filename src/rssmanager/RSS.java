/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssmanager;

/**
 *
 * @author Wise-SW
 */
public class RSS {
    private int id;
    private int itemCount;

    
    private String title,newsLink,rssLink;
    
    public RSS(String title, String link, String rssLink) {
        this.title = title;
        this.newsLink = link;
        this.rssLink = rssLink;
    }
    
    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }
    

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String link) {
        this.newsLink = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RSS() {
    }

    @Override
    public String toString() {
        return title;
    }

    

    
    
}
