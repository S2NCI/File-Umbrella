package Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SendReceiveController {

    // JavaFX fields
    @FXML
    public ListView fileListView;
    @FXML
    public Button btnUpload;
    @FXML
    public Label folderNameLabel;
    @FXML
    public FontAwesomeIconView uploadIcn;

    private ObservableList<File> fileList = FXCollections.observableArrayList();


    // JavaFX methods to handle events
    public void handleFileUpload(ActionEvent event) {
    }

    // opens the download scene
    public void handleDownloadScene(ActionEvent event) throws IOException {
        // open download scene
        Parent root = FXMLLoader.load(getClass().getResource("/download-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    // opens the settings scene
    public void handleSettingsWindow(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/settings-view.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        dragEvent.consume();
    }


    public void handleDragDrop(DragEvent dragEvent) {
        List<File> droppedFiles = dragEvent.getDragboard().getFiles();
        fileListView.setOnDragDropped(event -> {
            fileList.addAll(droppedFiles);
            fileListView.setItems(fileList);
        });
        dragEvent.setDropCompleted(true);
        dragEvent.consume();

        if(droppedFiles.size() > 0) {
            btnUpload.setVisible(false);
            btnUpload.setDisable(true);
            uploadIcn.setVisible(false);
        }

        fileListView.setCellFactory(param -> new ListCell<File>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(file.getName());
                    imageView.setImage(new Image(file.toURI().toString()));
                    setGraphic(imageView);
                }
            }
        });
    }
}
