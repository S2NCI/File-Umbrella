/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @authors Team 19
 */
public class Notification {
    public static void main(String[] args) throws AWTException {
        if (SystemTray.isSupported()) {
            Notification td = new Notification();
            td.displayTray();
        } else {
            System.err.println("System tray not supported!");
        }
    }
    
    public void displayTray() throws AWTException {
        // Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        // If the icon is a file
        Image image = null;
        try {
            File pathToFile = new File("Assets/File_Umbrella_Icon_Base.png");
            image = ImageIO.read(pathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrayIcon trayIcon = new TrayIcon(image);
        // Allow system to resize the image if required
        trayIcon.setImageAutoSize(true);
        // Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        
        // Send Notification
        tray.add(trayIcon);
        trayIcon.displayMessage("Hello, World", "notification demo", TrayIcon.MessageType.NONE);
        
    }
}
