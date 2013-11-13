
package rssmanager;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Wise-SW
 */
public class NewFeedDialog extends javax.swing.JDialog {
    
    private int return_status = -1;
    private int rssID = -1;
    /**
     * Creates new form NewFeedDialog
     */
    public int getReturn_status() {
        return return_status;
    }

    public void setReturn_status(int return_status) {
        this.return_status = return_status;
    }

    
    public NewFeedDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        try {
            String url = "";
            Clipboard clb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable trans = clb.getContents(null);
            if(trans.isDataFlavorSupported(DataFlavor.stringFlavor)){
                url = (String) clb.getData(DataFlavor.stringFlavor);
            }
            if(url.contains("http://") & url.contains("rss")){
                txtUrl.setText((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(NewFeedDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtUrl = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("   Add Feed");
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Add rss link to read new feed");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 10));
        jPanel1.add(jLabel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jLabel3.setText("Supplying the website or direct link to creat a feed");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 0));

        txtUrl.setText("http://");
        txtUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUrlActionPerformed(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.setFocusPainted(false);
        btnOk.setOpaque(false);
        btnOk.setRequestFocusEnabled(false);
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtUrl)
                .addGap(20, 20, 20))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel3)
                .addGap(0, 272, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(btnOk)
                .addGap(18, 18, 18)
                .addComponent(btnCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 0, 0)
                .addComponent(txtUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        rssID = getRssFeed();
        if(rssID >= 0){
            setReturn_status(rssID);
            setVisible(false);
            dispose();
        }else 
        {
            setReturn_status(-1);
            setVisible(false);
            dispose();
        }
        
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setReturn_status(-1);
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUrlActionPerformed
        rssID = getRssFeed();
        if(rssID >= 0){
            setReturn_status(rssID);
            setVisible(false);
            dispose();
        }else 
        {
            setReturn_status(-1);
            setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_txtUrlActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtUrl;
    // End of variables declaration//GEN-END:variables

    public int getRssFeed(){
        try {
            // TODO add your handling code here:
            URI uri = new URI(txtUrl.getText());
            int rssID;
            if(uri.isAbsolute()){
                RSSReader rSSReader = new RSSReader(uri.toString());
                if((rssID = rSSReader.loadRSS()) >= 0){
                    return rssID;
                }else{
                    JOptionPane.showMessageDialog(this, "Add Fail!");
                    return rssID;
                }
                    
            }
            else{
                txtUrl.setText("");
                JOptionPane.showMessageDialog(this, "Direct link is not valid!");
                return -1;
            }
        } catch (URISyntaxException ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage());
            return -1;
        }
    }
}
