/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Wise-SW
 */
public final class ShowNewFeed extends javax.swing.JFrame {
    
    private Vector<RSSItem> rSSItem;

    public ShowNewFeed(Vector<RSSItem> rSSItem) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ShowNewFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.rSSItem = rSSItem;
        initComponents();
        setMaximumSize(new Dimension(1280, 720));
        loadNewFeed();
    }

    public Vector<RSSItem> getrSSItem() {
        return rSSItem;
    }

    public void setrSSItem(Vector<RSSItem> rSSItem) {
        this.rSSItem = rSSItem;
    }
    
//    public static void main(String[] args){
//        ShowNewFeed showNewFeed = new ShowNewFeed(null);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        showNewFeed.setSize(500, 150);
//        showNewFeed.setLocation((screenSize.width-500)/2, (screenSize.height-150)/3);
//        showNewFeed.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent e) {
//                System.exit(0);
//            }
//        });
//        showNewFeed.setVisible(true);
//    }
    
    /**
     * Creates new form ShowNewFeed
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jpnTitle = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jpnTitle.setBackground(new java.awt.Color(255, 255, 255));
        jpnTitle.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitle.setText("New Feed");
        lblTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jpnTitle.add(lblTitle, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpnTitle, java.awt.BorderLayout.NORTH);

        pnContent.setLayout(new javax.swing.BoxLayout(pnContent, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane2.setViewportView(pnContent);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify                     
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel jpnTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnContent;
    // End of variables declaration                   
    
    public void loadNewFeed(){
        if (rSSItem.size() > 0) {
            for (int i = 0; i < rSSItem.size(); i++) {
                final int index = i;
                final String url = rSSItem.get(i).getLink();
                final JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.WHITE);
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                panel.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        panel.setBackground(new Color(220, 220, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        panel.setBackground(Color.WHITE);
                    }
                    
                    
                });
                final JLabel title = new JLabel(rSSItem.get(i).getTitle());
                title.setFont(new Font("Tahoma", Font.BOLD, 14));
                title.setCursor(new Cursor(Cursor.HAND_CURSOR));
                title.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/rss_feed.png")));
                title.setBorder(new EmptyBorder(2, 10, 2, 2));
                title.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent evt){
                        title.setText("<html><u>"+title.getText()+"</b></html>");
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent evt){
                        title.setText(rSSItem.get(index).getTitle());
                    }
                    
                    @Override
                    public void mouseClicked(MouseEvent evt){
                        if(SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount() == 1){
                            try {
                                URI uri = new URI(url);
                                Desktop desktop = Desktop.getDesktop();
                                desktop.browse(uri);
                            } catch (                    URISyntaxException | IOException ex) {
                                Logger.getLogger(ShowNewFeed.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                });
                
                JPanel pnDesc = new JPanel();
                pnDesc.setOpaque(false);
                pnDesc.setLayout(new BoxLayout(pnDesc, BoxLayout.PAGE_AXIS));
                
                JLabel desc = new JLabel("  " + rSSItem.get(i).getDesc());
                desc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
                
                JLabel author = new JLabel("  Author: " + rSSItem.get(i).getCreator());
                author.setFont(new Font("Arial", Font.PLAIN, 12));
                
                JLabel date = new JLabel("  Date: "+rSSItem.get(i).getDate());
                date.setFont(new Font("Arial", Font.PLAIN, 12));
                
                pnDesc.add(desc);
                pnDesc.add(author);
                pnDesc.add(date);
                
                panel.add(title, BorderLayout.NORTH);
                panel.add(pnDesc,BorderLayout.CENTER);
                
                pnContent.add(panel);
                pnContent.add(new JSeparator(JSeparator.HORIZONTAL));
            }
        }else{
            JLabel lblNull = new JLabel("No new feed!");
            lblNull.setFont(new Font("Tahoma", Font.BOLD, 24));
            lblNull.setForeground(Color.red);
            pnContent.add(lblNull);
        }
    }
}
