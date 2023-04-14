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

import javax.crypto.NullCipher;

public class SettingsController implements Initializable {

    @FXML
    public JFXToggleButton toggleUpdate;
    @FXML
    public JFXToggleButton toggleShare;
    @FXML
    public Button defaultDirBtn;
    @FXML
    public Button saveSettingsBtn;
    @FXML
    public TextField defaultDirectoryTextField;
    //file drectory location with default in documents
    public static Boolean autoShare;
    public static Boolean autoUpdate;
    public static String defaultDirectoryPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleUpdate.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                toggleUpdate.setText("Downloading is Enabled");
                autoUpdate = true;
            } else {
                toggleUpdate.setText("Downloading is Disabled");
                autoUpdate = false;
            }
        });
        toggleShare.selectedProperty().addListener((observableValue, aBoolean, t2) -> {
            if (t2) {
                toggleShare.setText("Uploading is Enabled");
                autoShare = true;
            } else {
                toggleShare.setText("Uploading is Disabled");
                autoShare = false;
            }
        });
        // Load the default directory setting from a file
        loadDefaultDirectory();
        toggleShare.setSelected(loadAutoShare());
        toggleUpdate.setSelected(loadAutoUpdate());
    }


    

    public void saveSettings(ActionEvent event) {
        // Save the default directory setting when the user clicks the "Save" button
        String defaultDirectory = defaultDirectoryTextField.getText();
        saveSettings(defaultDirectory, autoShare, autoUpdate);
        // Close the settings window
        Stage stage = (Stage) defaultDirectoryTextField.getScene().getWindow();
        stage.close();
    }

    //#region Default Directory

    public void handleDefaultDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Default Directory");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            // Update the default directory text field with the selected directory path
            defaultDirectoryTextField.setText(selectedDirectory.getAbsolutePath());
        }
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
        if(defaultDirectoryPath == null) {
            //generic value
            defaultDirectoryPath = System.getProperty("user.home") + "\\Documents\\File Umbrella";
        }
        defaultDirectoryTextField.setText(defaultDirectoryPath);
        return defaultDirectoryPath;
    }

    public Boolean loadAutoShare() {
        // Load the share setting from a file
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream("settings.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String autoShareRaw = props.getProperty("autoShare");
        if(autoShareRaw == null || autoShareRaw.matches("false")) {
            return false; 
        } else { 
            return true; 
        }
    }

    public Boolean loadAutoUpdate() {
        // Load the update setting from a file
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream("settings.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String autoUpdateRaw = props.getProperty("autoUpdate");
        if(autoUpdateRaw == null || autoUpdateRaw.matches("false")) {
            return false; 
        } else { 
            return true; 
        }
    }

    private void saveSettings(String dd, Boolean as, Boolean au) {
        // Save the settings to a properties file
        Properties props = new Properties();

        
        props.setProperty("defaultDirectory", dd);
        if(as == null || !as) {
            props.setProperty("autoShare", "false");
        } else { 
            props.setProperty("autoShare", "true");
        }
        if(au == null || !au) {
            props.setProperty("autoUpdate", "false");
        } else { 
            props.setProperty("autoUpdate", "true");
        }

        try (OutputStream outputStream = new FileOutputStream("settings.properties")) {
            props.store(outputStream, "Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}