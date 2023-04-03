package Controllers;

import UmbrellaPackage.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class UIFileController {

    @FXML
    Button btnUpload;

    @FXML
    Button sendSceneBtn;
    @FXML
    Button downloadSceneBtn;
    @FXML
    Button viewIncoming;

    @FXML
    private void handleFileUpload (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        // open file chooser dialog
        File file = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
        // set button text to the name of the file
        btnUpload.setText(file.getName());
    }

    @FXML
    private void handleSendScene (ActionEvent event) throws IOException {
        // open send scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/send-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1090, 514);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("File Umbrella");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void handleViewIncoming (ActionEvent event) throws IOException {

    }

    @FXML
    private void handleDownloadScene(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/download-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1090, 514);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("File Umbrella");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
