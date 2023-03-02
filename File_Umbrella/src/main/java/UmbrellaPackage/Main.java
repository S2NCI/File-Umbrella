/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * @authors Team 19
 */

public class Main {
    private ArrayList<Folder> folders;
    private static TrayInterface TI;
    
    public void main(String args[]) throws AWTException { 
        //create system tray icon and 
        if (SystemTray.isSupported()) {
            TI = new TrayInterface();
            TI.addIcon();
        } else {
            System.err.println("System tray not supported!");
        }
            
        // when file data is recieved
        String folder = "Folder";
        //TI.displayNotification(folder);
            
        //read local save data about folders
       // loadData();
            
    }
    
    //load saved folder data
    private void loadData() {
        try {
            FileInputStream fis = new FileInputStream("folders.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);   
            folders = (ArrayList<Folder>) ois.readObject();
            ois.close(); 
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    //save folder data to file
    private void saveData() {
        try {
            FileOutputStream fos = new FileOutputStream("folders.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);   
            oos.writeObject(folders);
            oos.close(); 
        } catch (IOException e) {
            System.out.println(e);
        } 
    }
}
