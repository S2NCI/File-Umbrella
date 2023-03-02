/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @authors Team 19
 */
public class TrayInterface {
    static Image iconImage;
    static TrayIcon trayIcon;
    
    protected static void TrayInterface(String[] args) throws AWTException {
        if (SystemTray.isSupported()) {
            
            /*trayIcon.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        try {
                            td.displayNotification("[Folder]");
                        } 
                        catch (AWTException ex) {
                            Logger.getLogger(TrayInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });*/
        } else {
            System.err.println("System tray not supported!");
        }
        
        
    }
    
    public void addIcon() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        
        // Create a usable icon from the image file
        try {
            File pathToFile = new File("Assets/File_Umbrella_Icon_Base.png");
            iconImage = ImageIO.read(pathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        trayIcon = new TrayIcon(iconImage);
        // Allow system to resize the image if required
        trayIcon.setImageAutoSize(true);
        // Set tooltip text for the tray icon
        trayIcon.setToolTip("File Umbrella");
        
        tray.add(trayIcon);
    }
    
    public void displayNotification(String folderName) throws AWTException {
        // Obtain only one instance of the SystemTray object
        
        // Send Notification
        trayIcon.displayMessage("Folder Ready to Sync", folderName + " has been updated by another device, click to sync.", TrayIcon.MessageType.NONE);
    }
}
