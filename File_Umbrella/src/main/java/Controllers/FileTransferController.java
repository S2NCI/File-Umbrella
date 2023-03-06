package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;

public class FileTransferController {

    @FXML
    Button btnUpload;

    @FXML
    private void handleFileUpload (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        // open file chooser dialog
        File file = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
        // set button text to the name of the file
        btnUpload.setText(file.getName());
    }

}
