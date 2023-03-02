/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @authors Team 19
 */
public class TrayInterface {
    static Image iconImage;
    static TrayIcon trayIcon;
    
    protected void addIcon() throws AWTException {
        //method to add the program icon to the taskbar
        SystemTray tray = SystemTray.getSystemTray();
        Frame frame = new Frame("");
        
        // Create a usable icon from the image file
        try {
            File pathToFile = new File("Assets/File_Umbrella_Icon_Base.png");
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
                    // open jframe here
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
        final PopupMenu popup = new PopupMenu("File Umbrella");
        MenuItem creditItem = new MenuItem("Acknowlegments");
        MenuItem exitItem = new MenuItem("Close File Umbrella");
        // Add components to pop-up menu
        popup.addSeparator();
        popup.add(creditItem);
        popup.add(exitItem);
        
        //add action listeners for clicking the popup items
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
