package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    public JFXToggleButton toggleSync;
    @FXML
    public Button defaultDirBtn;
    @FXML
    public Button saveSettingsBtn;
    @FXML
    public TextField defaultDirectoryTextField;
    public String defaultDirectoryPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleSync.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                toggleSync.setText("Sync is on");
            } else {
                toggleSync.setText("Sync is off");
            }
        });
        // Load the default directory setting from a file
        loadDefaultDirectory();
    }


    public void handleDefaultDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Default Directory");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            // Update the default directory text field with the selected directory path
            defaultDirectoryTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void saveSettings(ActionEvent event) {
        // Save the default directory setting when the user clicks the "Save" button
        String defaultDirectory = defaultDirectoryTextField.getText();
        saveDefaultDirectory(defaultDirectory);
        // Close the settings window
        Stage stage = (Stage) defaultDirectoryTextField.getScene().getWindow();
        stage.close();
    }

    // Load the default directory setting from a file
    public String loadDefaultDirectory() {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream("settings.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultDirectoryPath = props.getProperty("defaultDirectory");
        defaultDirectoryTextField.setText(defaultDirectoryPath);
        return defaultDirectoryPath;
    }


    private void saveDefaultDirectory(String defaultDirectory) {
        // Save the default directory setting to a file
        Properties props = new Properties();
        props.setProperty("defaultDirectory", defaultDirectory);
        try (OutputStream outputStream = new FileOutputStream("settings.properties")) {
            props.store(outputStream, "Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
