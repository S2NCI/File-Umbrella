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

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Team 19
 */

public class Main extends Application {
    private static final String LAST_VIEW = "lastView";
    private TrayIcon trayIcon;
    @FXML
    private Button settingsBtn;
    private Properties properties = new Properties();
    private File propertiesFile = new File("app.properties");

    @Override
    public void start(Stage stage) throws AWTException, IOException {
        Image fxImage = new Image("FileUmbrellaAppIcon.png");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/join-view.fxml"));
            Image appIcon = new Image("FileUmbrellaAppIcon.png");
            stage.getIcons().add(appIcon);
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
        launch();
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
}
