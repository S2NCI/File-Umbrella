/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.application.Application.launch;

/**
 * @author Team 19
 */

public class Main extends Application {
    private ArrayList<Folder> folders;
    private static TrayInterface TI;

    public void start(Stage stage) throws AWTException, IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/join-view.fxml"));
            Image image = new Image("FileUmbrellaAppIcon.png");
            stage.getIcons().add(image);
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("File Umbrella");
            // add the image from the res folder as the icon
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    public void openEnvelope(Envelope envelope, String sourceIP) {
        int destinationID = envelope.getId();
        boolean request = envelope.isRequest();
        
        //find matching folder id
        for(Folder f : folders) {
            if(!f.getID().matches(String.valueOf(destinationID))) continue;
            
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

    /*
    //load saved folder data
    private void loadData() {
        try {
            FileInputStream fis = new FileInputStream("folders.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            folders = (ArrayList<Folder>) ois.readObject();
            ois.close();
        } catch (Exception e) {
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
     */
    
    /*
    //create system tray icon and 
        if (SystemTray.isSupported()) {
            TI = new TrayInterface();
            TI.addIcon();
        } else {
            System.err.println("System tray not supported!");
     File f = new File("C:");//temporary
        Folder folder = new Folder(f, 10, "ay", false, false); //temporary
        
        TI.displayNotification(folder.getFolderName());
            
        //read local save data about folders
       // loadData();
    */
}
