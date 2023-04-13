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
    public JFXToggleButton toggleCompression;
    @FXML
    public Button saveSettingsBtn;
    @FXML
    public TextField defaultDirectoryTextField;
    public String defaultDirectoryPath;
    public boolean syncStatus;
    private boolean autoSyncEnabled = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleSync.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                toggleSync.setText("Sync is on");
                autoSyncEnabled = true;
            } else {
                toggleSync.setText("Sync is off");
                autoSyncEnabled = false;
            }

        });
        toggleCompression.setOnAction(event -> {
            if (toggleCompression.getText().equals("Compression is on")) {
                toggleCompression.setText("Compression is off");
            } else {
                toggleCompression.setText("Compression is on");
            }
        });
        // Load the default directory setting from a file
        loadDefaultDirectory();
        // Load the auto sync setting from a file
        loadAutoSyncSetting();
    }



    public void handleDefaultDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            defaultDirectoryTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void saveSettings(ActionEvent event) {
        // Save the default directory setting when the user clicks the "Save" button
        String defaultDirectory = defaultDirectoryTextField.getText();
        saveDefaultDirectory(defaultDirectory);
        // Save the auto sync setting to a file
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

    // method to load auto sync setting
    public boolean loadAutoSyncSetting() {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream("settings.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        syncStatus = Boolean.parseBoolean(props.getProperty("autoSyncEnabled"));
        toggleSync.setSelected(syncStatus);
        // return syncStatus and set status to true
        return syncStatus;
    }

    private void saveDefaultDirectory(String defaultDirectory) {
        // Save the default directory setting to a file
        Properties props = new Properties();
        props.setProperty("defaultDirectory", defaultDirectory);
        // Save the auto sync setting to a file
        props.setProperty("autoSyncEnabled", Boolean.toString(toggleSync.isSelected()));
        try (OutputStream outputStream = new FileOutputStream("settings.properties")) {
            props.store(outputStream, "Settings");
            // save auto sync setting
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
