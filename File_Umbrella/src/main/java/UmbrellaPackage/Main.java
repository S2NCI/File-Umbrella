/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @authors Team 19
 */

public class Main {
    private ArrayList<Folder> folders;
    private static TrayInterface TI;
    
    public static void main(String args[]) throws AWTException { 
        //create system tray icon and 
        if (SystemTray.isSupported()) {
            TI = new TrayInterface();
            TI.addIcon();
        } else {
            System.err.println("System tray not supported!");
        }
            
        File f = new File("C:");//temporary
        Folder folder = new Folder(f, 10, "ay", false, false); //temporary
        
        TI.displayNotification(folder.getFolderName());
            
        //read local save data about folders
       // loadData();
            
    }
    
    public void openEnvelope(Envelope envelope, String sourceIP) {
        int destinationID = envelope.getId();
        boolean request = envelope.isRequest();
        
        //find matching folder id
        for(Folder f : folders) {
            if(f.getID() != destinationID) continue;
            
            boolean memberExists = false;
            for(String m : f.getMembers()) {
                if(m.matches(sourceIP)) {
                    memberExists = true;
                    break;
                }
            }
            //if no member was found add the source ip
            if(!memberExists) f.addMember(sourceIP);
            
            
            if(request) {
                //if a file request pass to send method
                f.sendFiles(envelope.getSentFiles(), sourceIP);
                
            } else {
                //if not is an update list(send to request method)
                f.requestFiles(envelope.getSentFiles(), sourceIP);
            }
        }
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
