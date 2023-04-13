package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private JFXToggleButton toggleSync;
    @FXML
    private Button defaultDirBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleSync.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                toggleSync.setText("Sync is on");
            } else {
                toggleSync.setText("Sync is off");
            }
        });
    }
}
