/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @authors Team 19
 */

public class TrayInterface {
    private static Image iconImage;
    private static TrayIcon trayIcon;
    
    protected void addIcon() throws AWTException {
        //method to add the program icon to the taskbar
        SystemTray tray = SystemTray.getSystemTray();
        Frame frame = new Frame("");
        
        // Create a usable icon from the image file
        try {
            File pathToFile = new File("File_Umbrella_Icon_Base.png");
            iconImage = ImageIO.read(pathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // create a right-click popup menu
        PopupMenu popup = createPopupMenu();
        
        trayIcon = new TrayIcon(iconImage, "File Umbrella", popup);
        // Allow system to resize the image if required
        trayIcon.setImageAutoSize(true);
        
        //add mouse click context actions to the icon
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    // open login scene
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/join-view.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(e.getButton() == MouseEvent.BUTTON3) {
                    frame.add(popup);
                    popup.show(frame, e.getXOnScreen(), e.getYOnScreen());
                } 
            }
        });
        tray.add(trayIcon);
    }
    
    protected void displayNotification(String folderName) throws AWTException {
        //send notification about folder update
        trayIcon.displayMessage("Folder Ready to Sync", folderName + " has been updated by another device, click to sync.", TrayIcon.MessageType.NONE);
    }
    
    protected static PopupMenu createPopupMenu() {
        final PopupMenu popup = new PopupMenu();
        MenuItem header = new MenuItem("File Umbrella");
        MenuItem creditItem = new MenuItem("Acknowlegments");
        MenuItem exitItem = new MenuItem("Exit");
        // Add components to pop-up menu
        popup.add(header);
        popup.addSeparator();
        popup.add(creditItem);
        popup.add(exitItem);
        
        //add action listeners for clicking the popup items
        header.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open jframe
            }
        });
        creditItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open jframe but team members and sources panel here
            }
        });
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //terminate the program entirely
                System.exit(0);
            }
        });
        
        return popup;
    }
}
