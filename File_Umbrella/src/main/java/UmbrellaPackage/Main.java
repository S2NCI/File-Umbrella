/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;
import java.awt.AWTException;
import java.awt.SystemTray;

/**
 *
 * @authors Team 19
 */
public class Main {
    public static void main(String args[]) throws AWTException { 
        if (SystemTray.isSupported()) {
            TrayInterface TI = new TrayInterface();
            TI.addIcon();
        } else {
            System.err.println("System tray not supported!");
        }
            
            // when file data is recieved
            String folder = "Folder";
            //TI.displayNotification(folder);
            
    }
}
