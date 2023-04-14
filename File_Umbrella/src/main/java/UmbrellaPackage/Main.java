/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package UmbrellaPackage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import com.jcraft.jsch.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Properties;

/**
* @author Team 19
*/

public class Main extends Application {

    private static TrayIcon trayIcon;
    private static ArrayList<Folder> folders;
    /*private static String folderPath = System.getProperty("user.home") + "\\Documents\\File Umbrella";*/
    private static String folderPath = Controllers.SettingsController.defaultDirectoryPath + "\\";
    private static final String LAST_VIEW = "lastView";

    @FXML
    private Button settingsBtn;
    private Properties properties = new Properties();
    private File propertiesFile = new File("app.properties");

    @Override
    public void start(Stage stage) throws AWTException, IOException {
        //Image fxImage = new Image("File_Umbrella_App_Base.png");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/join-view.fxml"));
            Image image = new Image("File_Umbrella_Icon_Base.png");
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
        // if program is minimized to tray, try to open last view - or default view "join-view.fxml"
        AtomicReference<String> viewName = new AtomicReference<>(properties.getProperty("lastView", "join-view.fxml"));


        // intercepting the request to close the applicaion and minimising instead
        Stage finalStage = stage;
        stage.setOnCloseRequest(event -> {
            event.consume();
            finalStage.hide();
            // saving folder data
            saveData();
            // saving the last view to the properties file
            properties.setProperty("lastView", viewName.get());
            try {
                properties.store(new FileOutputStream(propertiesFile), "Application Properties");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // showing the stage
        stage.show();

        // if program is minimized to tray, create tray icon
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            // creating the popup menu
            PopupMenu popup = new PopupMenu();

            // creating the menu items
            MenuItem openLast = new MenuItem("Open");
            MenuItem openLogin = new MenuItem("Login to Folder");
            MenuItem openCreate = new MenuItem("Create Folder");
            MenuItem exitApp = new MenuItem("Exit");

            popup.add(openLast);
            popup.addSeparator();
            popup.add(openLogin);
            popup.add(openCreate);
            popup.add(exitApp);

            Stage finalStage1 = stage;
            openLast.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String lastView = getLastView();
                    if (lastView != null) {
                        Platform.runLater(() -> {
                            handleStageMinimizedToTray();
                        });
                    }
                }
            });

            openLogin.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(() -> {
                        try {
                            viewName.set("/join-view.fxml");
                            FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewName.get()));
                            Parent root = loader.load();
                            stage.setScene(new Scene(root));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            });

            openCreate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(() -> {
                        try {
                            viewName.set("/createfolder-view.fxml");
                            FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewName.get()));
                            Parent root = loader.load();
                            stage.setScene(new Scene(root));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            });
            exitApp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
    }

    public static void main(String[] args) {
        //split backend into their own concurrent starting thread
        Thread threadBack = new Thread() {
            public void run() {
                createDirectory();
                loadData();
                ChangeMonitor cm = new ChangeMonitor();
            }
        };

        //start the threads
        threadBack.start();
        launch();
    }

    public void openEnvelope(Envelope envelope, String sourceIP) {
        String destinationID = envelope.getId();
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
        }
    }

    public String getLastView() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return prefs.get(LAST_VIEW, null);
    }

    public void storeLastView(String lastView) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put(LAST_VIEW, lastView);
    }

    private void handleStageMinimizedToTray() {
        TrayIcon trayIcon = SystemTray.getSystemTray().getTrayIcons()[0];
        trayIcon.addActionListener(e -> {
            Platform.runLater(() -> {
                // Retrieve the name of the last view from the preferences
                Preferences prefs = Preferences.userNodeForPackage(Main.class);
                String lastView = prefs.get(LAST_VIEW, null);
                if (lastView != null) {
                    try {
                        // Load and show the last view
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(lastView));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });
   }

    private static void loadData() {
        //load saved folder data
        try {
            FileInputStream fis = new FileInputStream("folders.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            folders = (ArrayList<Folder>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addFolder(String folderName, String id, String accessPassword, boolean autoUpdate, boolean autoShare) {
        //called when creating or joining a new folder
        Folder f = new Folder(folderName, id, accessPassword, autoUpdate, autoShare);
        folders.add(f);
    }

    public void deleteDirectory() {
        //method to delete a folder when removed from the sync list
        
    }

    private static void saveData() {
        //save folder data to file
        try {
            FileOutputStream fos = new FileOutputStream("folders.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(folders);
            oos.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
   
    private static void createDirectory() {
        //method to create a directory folder to manage files from
        File directory = new File(folderPath);
    
        try {
            directory.mkdir();
            Files.setAttribute(Paths.get(folderPath), "dos:system", true, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            // Folder might already exist, that's fine
        }
        
        placeINI();
        //placeShortcut();
    }

    private static void placeINI() {
        //method to create a custom directory folder icon on startup
        String iniPath = folderPath + "\\desktop.ini";
        File iniFile = new File(iniPath);

        //place icon resource in mobile location
        String iconPath = "src\\main\\resources\\File_Umbrella_Folder.ico";
        Path iconSource = Paths.get(iconPath);
        Path iconDest = Paths.get(folderPath + "\\Folder.ico");
        try {
            Files.copy(iconSource, iconDest);
            Files.setAttribute(iconDest, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            // Folder might already exist, that's fine
        }

        if (!iniFile.exists()) {
            try {
                FileWriter writer = new FileWriter(iniFile);
                writer.write("[.ShellClassInfo]\r\n");
                writer.write("ConfirmFileOp=0\r\n");
                writer.write("IconResource=" + folderPath + "\\Folder.ico,0\r\n");
                writer.close();
        
                File folder = new File(folderPath);
                folder.setExecutable(true);
                folder.setWritable(true);
                folder.setReadable(true);
        
                File desktopINI = new File(folderPath + "\\desktop.ini");
                desktopINI.setExecutable(true);
                desktopINI.setWritable(false);
                desktopINI.setReadable(true);
        
                Files.setAttribute(Paths.get(iniPath), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
                Files.setAttribute(Paths.get(iniPath), "dos:system", true, LinkOption.NOFOLLOW_LINKS);
        
            } catch (IOException e) {
                // Folder might already exist, that's fine
            }
        }
    }

    protected void displayNotification(String folderName) throws AWTException {
        //send notification about folder update
        trayIcon.displayMessage("Folder Ready to Sync", folderName + " has been updated by another device, click to sync.", TrayIcon.MessageType.NONE);
    }
    
    /*private static void placeShortcut() { 
        //method to create a more visible shortcut to the directory folder
        Path folderPath = Path.of(userHome + "\\Documents\\File Umbrella");

        // Create a shortcut file for the folder
        Path shortcutPath = Path.of(userHome + "\\Links\\File Umbrella.lnk");
        File shortcutFile = shortcutPath.toFile();
        shortcutFile.createNewFile();

        // Set the shortcut file attributes to make it a shortcut
        Files.setAttribute(shortcutPath, "dos:system", true);
        Files.setAttribute(shortcutPath, "dos:hidden", true);

        // Set the shortcut target to the folder
        ShellLink.createLink(folderPath.toString(), shortcutPath.toString());

        //maybe if we decide to be 50% more annoying, will force the app into quick access
    }*/
   
    /* Depreciated due to FX issues in seperate class
    private static void startTray() throws AWTException {
        TrayInterface TI;
        //create system tray icon and prepare for sync notifications
        if (SystemTray.isSupported()) {
            TI = new TrayInterface();
            TI.addIcon();
        } else {
            System.err.println("System tray not supported!");
        }

            //File f = new File("C:");//temporary
        
        //TI.displayNotification(folder.getFolderName());
    }*/
}