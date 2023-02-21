package UmbrellaPackage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author spart
 */
public class Interface extends javax.swing.JFrame {

    private ArrayList<GiftIdea> giftIdeas;

    public Interface() {
        initComponents();
        giftIdeas = new ArrayList<>();
        
        //fill the arraylists with some values
        //recip-desc-url 
        /* retaining commented incase of file error
        callableAdd("Wayne", "A pair of wellies", "boots.ie");
        callableAdd("Lucas", "Assorted Legos", "https://www.lego.com/en-ie/product/lego-medium-creative-brick-box-10696?gclid=Cj0KCQiAsoycBhC6ARIsAPPbeLt5ISFVPtQM4Kqaxem2sa8fbDWAltIlG0CdY7L0j8AjwirHj8_HEwgaAmCGEALw_wcB&ef_id=Cj0KCQiAsoycBhC6ARIsAPPbeLt5ISFVPtQM4Kqaxem2sa8fbDWAltIlG0CdY7L0j8AjwirHj8_HEwgaAmCGEALw_wcB:G:s&s_kwcid=AL!933!3!627500590052!!!g!340007787760!!18564623980!145269219347&cmp=KAC-INI-GOOGEU-");
        callableAdd("Hannah", "Hobby tools", "https://help.cricut.com/hc/article_attachments/360059434653/QuickSwap_tools.png");
        callableAdd("Sam", "Clothing", "https://www.primark.com/en-ie");
        callableAdd("Kathleen", "A book", "https://www.easons.com/Forever-Home-graham-norton-9781529391404-TPB-en-us.aspx");
        */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainTabbedPane = new javax.swing.JTabbedPane();
        Skills = new javax.swing.JPanel();
        SearchNameLabel = new javax.swing.JLabel();
        SearchIdeaLabel = new javax.swing.JLabel();
        SearchAddressLabel = new javax.swing.JLabel();
        SearchRemoveButton = new javax.swing.JButton();
        SearchDoButton = new javax.swing.JButton();
        SearchClearButton = new javax.swing.JButton();
        SearchAddButton = new javax.swing.JButton();
        SearchIdeaField = new javax.swing.JTextField();
        SearchNameField = new javax.swing.JTextField();
        SearchAddressField = new javax.swing.JTextField();
        Ownership = new javax.swing.JPanel();
        OwnershipItemNameLabel = new javax.swing.JLabel();
        ListSaveButton = new javax.swing.JButton();
        ListRefreshButton = new javax.swing.JButton();
        OwnershipScrollPane = new javax.swing.JScrollPane();
        GiftList = new javax.swing.JTextArea();
        ListLoadButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 204, 255));

        MainTabbedPane.setBackground(new java.awt.Color(0, 102, 255));
        MainTabbedPane.setForeground(new java.awt.Color(255, 255, 255));
        MainTabbedPane.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        Skills.setBackground(new java.awt.Color(153, 153, 255));
        Skills.setLayout(null);

        SearchNameLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        SearchNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        SearchNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        SearchNameLabel.setText("Recipient Name");
        Skills.add(SearchNameLabel);
        SearchNameLabel.setBounds(25, 40, 100, 25);

        SearchIdeaLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        SearchIdeaLabel.setForeground(new java.awt.Color(255, 255, 255));
        SearchIdeaLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        SearchIdeaLabel.setText("Gift Idea");
        Skills.add(SearchIdeaLabel);
        SearchIdeaLabel.setBounds(25, 80, 100, 25);

        SearchAddressLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        SearchAddressLabel.setForeground(new java.awt.Color(255, 255, 255));
        SearchAddressLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        SearchAddressLabel.setText("URL");
        Skills.add(SearchAddressLabel);
        SearchAddressLabel.setBounds(25, 200, 100, 25);

        SearchRemoveButton.setBackground(new java.awt.Color(0, 102, 255));
        SearchRemoveButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        SearchRemoveButton.setForeground(new java.awt.Color(255, 255, 255));
        SearchRemoveButton.setText("Remove");
        SearchRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchRemoveButtonActionPerformed(evt);
            }
        });
        Skills.add(SearchRemoveButton);
        SearchRemoveButton.setBounds(265, 270, 100, 35);

        SearchDoButton.setBackground(new java.awt.Color(0, 102, 255));
        SearchDoButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        SearchDoButton.setForeground(new java.awt.Color(255, 255, 255));
        SearchDoButton.setText("Search");
        SearchDoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchDoButtonActionPerformed(evt);
            }
        });
        Skills.add(SearchDoButton);
        SearchDoButton.setBounds(25, 270, 100, 35);

        SearchClearButton.setBackground(new java.awt.Color(0, 102, 255));
        SearchClearButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        SearchClearButton.setForeground(new java.awt.Color(255, 255, 255));
        SearchClearButton.setText("Clear All");
        SearchClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchClearButtonActionPerformed(evt);
            }
        });
        Skills.add(SearchClearButton);
        SearchClearButton.setBounds(390, 270, 100, 35);

        SearchAddButton.setBackground(new java.awt.Color(0, 102, 255));
        SearchAddButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        SearchAddButton.setForeground(new java.awt.Color(255, 255, 255));
        SearchAddButton.setText("Add");
        SearchAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchAddButtonActionPerformed(evt);
            }
        });
        Skills.add(SearchAddButton);
        SearchAddButton.setBounds(145, 270, 100, 35);

        SearchIdeaField.setBackground(new java.awt.Color(255, 255, 255));
        SearchIdeaField.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        SearchIdeaField.setForeground(new java.awt.Color(51, 51, 51));
        SearchIdeaField.setText("Enter Gift Idea");
        Skills.add(SearchIdeaField);
        SearchIdeaField.setBounds(145, 80, 345, 105);

        SearchNameField.setBackground(new java.awt.Color(255, 255, 255));
        SearchNameField.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        SearchNameField.setForeground(new java.awt.Color(51, 51, 51));
        SearchNameField.setText("Enter Recipient Name");
        Skills.add(SearchNameField);
        SearchNameField.setBounds(145, 40, 345, 25);

        SearchAddressField.setBackground(new java.awt.Color(255, 255, 255));
        SearchAddressField.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        SearchAddressField.setForeground(new java.awt.Color(51, 51, 51));
        SearchAddressField.setText("Enter Gift Reference URL");
        Skills.add(SearchAddressField);
        SearchAddressField.setBounds(145, 200, 345, 25);

        MainTabbedPane.addTab("Search", Skills);

        Ownership.setBackground(new java.awt.Color(153, 153, 255));
        Ownership.setLayout(null);

        OwnershipItemNameLabel.setText(".");
        Ownership.add(OwnershipItemNameLabel);
        OwnershipItemNameLabel.setBounds(205, 132, 71, 23);

        ListSaveButton.setBackground(new java.awt.Color(0, 102, 255));
        ListSaveButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ListSaveButton.setForeground(new java.awt.Color(255, 255, 255));
        ListSaveButton.setText("Save");
        ListSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListSaveButtonActionPerformed(evt);
            }
        });
        Ownership.add(ListSaveButton);
        ListSaveButton.setBounds(330, 270, 100, 35);

        ListRefreshButton.setBackground(new java.awt.Color(0, 102, 255));
        ListRefreshButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ListRefreshButton.setForeground(new java.awt.Color(255, 255, 255));
        ListRefreshButton.setText("Refresh");
        ListRefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListRefreshButtonActionPerformed(evt);
            }
        });
        Ownership.add(ListRefreshButton);
        ListRefreshButton.setBounds(90, 270, 100, 35);

        OwnershipScrollPane.setBackground(new java.awt.Color(0, 102, 255));

        GiftList.setBackground(new java.awt.Color(255, 255, 255));
        GiftList.setColumns(20);
        GiftList.setForeground(new java.awt.Color(51, 51, 51));
        GiftList.setRows(5);
        OwnershipScrollPane.setViewportView(GiftList);

        Ownership.add(OwnershipScrollPane);
        OwnershipScrollPane.setBounds(20, 40, 480, 215);

        ListLoadButton.setBackground(new java.awt.Color(0, 102, 255));
        ListLoadButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ListLoadButton.setForeground(new java.awt.Color(255, 255, 255));
        ListLoadButton.setText("Load");
        ListLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListLoadButtonActionPerformed(evt);
            }
        });
        Ownership.add(ListLoadButton);
        ListLoadButton.setBounds(210, 270, 100, 35);

        MainTabbedPane.addTab("List", Ownership);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ListRefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListRefreshButtonActionPerformed
        //call the list printer
        listShow();
    }//GEN-LAST:event_ListRefreshButtonActionPerformed

    private void ListSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListSaveButtonActionPerformed
        try {
            FileOutputStream fos = new FileOutputStream("gifts.lst");
            ObjectOutputStream oos = new ObjectOutputStream(fos);   
            oos.writeObject(giftIdeas);
            oos.close(); 
            JOptionPane.showMessageDialog(null, "List Saved Successfully");
        } catch (IOException e) {
            System.out.println(e);
        } 
    }//GEN-LAST:event_ListSaveButtonActionPerformed

    private void SearchAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchAddButtonActionPerformed
        //Start of insert button
        boolean validEntry = true;    
        String missingData = "Invalid Idea Values";
        
        String addRecipient = SearchNameField.getText();
        String addIdea = SearchIdeaField.getText();
        String addURL = SearchAddressField.getText();
        
        if (addRecipient.matches("Enter Recipient Name") || addRecipient.length()==0|| addRecipient == null) 
        {
            missingData = String.join("\n", missingData, "No gift recipient provided");
            validEntry = false;  
        }
        if (addIdea.matches("Enter Gift Idea") || addIdea.length()==0 || addIdea == null) 
        {
            missingData = String.join("\n", missingData, "No gift description provided");
            validEntry = false;  
        }
        if (addURL.matches("Enter Gift Reference URL") || addURL.length()==0 || addURL == null) 
        {
            missingData = String.join("\n", missingData, "No gift reference URL provided");
            validEntry = false;  
        }
        
        if (validEntry) {
            callableAdd(addRecipient, addIdea, addURL);
            JOptionPane.showMessageDialog(null, "New Gift Idea Added");
        } else {
            JOptionPane.showMessageDialog(null, missingData);
        }
        
        
    }//GEN-LAST:event_SearchAddButtonActionPerformed
    
    private void SearchClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchClearButtonActionPerformed
        SearchNameField.setText("Enter Recipient Name");  //set recipient request in textbox
        SearchIdeaField.setText("Enter Gift Idea"); //set idea request in textbox
        SearchAddressField.setText("Enter Gift Reference URL");  //set url request in textbox
    }//GEN-LAST:event_SearchClearButtonActionPerformed

    private void SearchDoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchDoButtonActionPerformed
        boolean found = false;
        
        for (GiftIdea g : giftIdeas) {
            //recipient as a unique identifier is a bit iffy but will return a consistent value
            if (g.getGiftRecipientName().matches(SearchNameField.getText())) {
                SearchNameField.setText(g.getGiftRecipientName());  //set recipient request in textbox
                SearchIdeaField.setText(g.getGiftIdeaText()); //set idea request in textbox
                SearchAddressField.setText(g.getGiftURL());  //set url request in textbox
                
                found = true;
            }
            
            if (found) {
                break;
            }
        }
        
        if (!found)
        JOptionPane.showMessageDialog(null, "No gift recipients matching the entered name");
    }//GEN-LAST:event_SearchDoButtonActionPerformed
    
    private void SearchRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchRemoveButtonActionPerformed
        for (GiftIdea g : giftIdeas) {
            if (!g.getGiftRecipientName().matches(SearchNameField.getText())) continue;
            if (!g.getGiftIdeaText().matches(SearchIdeaField.getText())) continue;
            if (!g.getGiftURL().matches(SearchAddressField.getText())) continue;
            
            //remove the first result with all matching values
            giftIdeas.remove(g);
            break;
        }
    }//GEN-LAST:event_SearchRemoveButtonActionPerformed

    private void ListLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListLoadButtonActionPerformed
        try {
            FileInputStream fis = new FileInputStream("gifts.lst");
            ObjectInputStream ois = new ObjectInputStream(fis);   
            giftIdeas = (ArrayList<GiftIdea>) ois.readObject();
            ois.close(); 
        } catch(Exception e) {
            System.out.println(e);
        }
        
        //show the list once imported
        listShow();
    }//GEN-LAST:event_ListLoadButtonActionPerformed

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

}
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }
    
    private void listShow() {
        GiftList.setText(null);//sets the list
        GiftList.append("Recipient \t Gift Idea \t Gift URL \n");//sets the list

        for (GiftIdea g : giftIdeas) {
            GiftList.append("" 
                    + g.getGiftRecipientName() + "\t" 
                    + g.getGiftIdeaText() + "\t" 
                    + g.getGiftURL() + "\n");//append to list
        }
    }
    
    private void callableAdd(String r, String i, String u) {
        GiftIdea g = new GiftIdea(r,i,u);
        giftIdeas.add(g);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea GiftList;
    private javax.swing.JButton ListLoadButton;
    private javax.swing.JButton ListRefreshButton;
    private javax.swing.JButton ListSaveButton;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JPanel Ownership;
    private javax.swing.JLabel OwnershipItemNameLabel;
    private javax.swing.JScrollPane OwnershipScrollPane;
    private javax.swing.JButton SearchAddButton;
    private javax.swing.JTextField SearchAddressField;
    private javax.swing.JLabel SearchAddressLabel;
    private javax.swing.JButton SearchClearButton;
    private javax.swing.JButton SearchDoButton;
    private javax.swing.JTextField SearchIdeaField;
    private javax.swing.JLabel SearchIdeaLabel;
    private javax.swing.JTextField SearchNameField;
    private javax.swing.JLabel SearchNameLabel;
    private javax.swing.JButton SearchRemoveButton;
    private javax.swing.JPanel Skills;
    // End of variables declaration//GEN-END:variables
}
