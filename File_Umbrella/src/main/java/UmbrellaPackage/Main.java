/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("File Umbrella");
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
}
