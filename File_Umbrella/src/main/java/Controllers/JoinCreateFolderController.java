package Controllers;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import UmbrellaPackage.Main;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.bson.Document;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.UUID;

public class JoinCreateFolderController implements Initializable {

    // JavaFX fields
    Main m = new Main();
    @FXML
    public TextField folderIDTF;
    @FXML
    public TextField folderPassTF;
    @FXML
    public Label btnCreateFolder;
    @FXML
    public TextField folderGen;
    @FXML
    public TextField folderPass;
    @FXML
    public TextField folderNameTF;

    @FXML
    public ProgressBar loginProgress;
    private Timeline timeline;
    private DoubleProperty progress = new SimpleDoubleProperty(0);

    @FXML
    private void handleAccountAuth(ActionEvent event) throws IOException {

        String folderId = folderIDTF.getText();
        String folderPassword = folderPassTF.getText();

        try {
            // establish connection to MongoDB database
            String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
            MongoClient mongoClient = DBController.createConnection(connectionString);
            MongoDatabase database = mongoClient.getDatabase("UserConnection");
            MongoCollection<Document> folderCollection = database.getCollection("FolderCollection");

            BasicDBObject query = new BasicDBObject();
            query.put("folderId", folderId);
            query.put("folderPassword", folderPassword);

            Document result = folderCollection.find(query).first();


            // open send scene when folder ID and password match
            if (result != null) {
                try {

                    Parent root = FXMLLoader.load(getClass().getResource("/send-view.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Folder ID or Password is incorrect");
                alert.setContentText("Please try again");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBController.closeConnection();
        }
    }


    // open create folder scene when create folder button is clicked
    @FXML
    private void handleCreateFolder(MouseEvent event) throws IOException {
        m.storeLastView("createfolder-view.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("/createfolder-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // cancel folder creation
    @FXML
    private void cancelCreate(MouseEvent event) throws IOException {
        m.storeLastView("join-view.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("/join-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // generate folder ID using UUID and display it in the text field
    public String folderIDGeneration(MouseEvent event) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String generated = uuid.substring(0, 16);
        folderGen.setText(generated);
        return uuid;
    }

    // copy folder ID to clipboard when icon is clicked
    public void copyFolderID(MouseEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(folderGen.getText());
        clipboard.setContent(content);
    }

    // create folder in database
    @FXML
    private void handleFolderCreation(ActionEvent event) throws IOException {
        String folderID = folderGen.getText();
        String folderPassword = folderPass.getText();
        String folderName = folderNameTF.getText();
        // get user ip
        String currentIP = null;
        try {
            currentIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // establish connection to MongoDB database
        String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = DBController.createConnection(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserConnection");
        MongoCollection<Document> collection = database.getCollection("FolderCollection");

        if (!folderName.isEmpty() && !folderPassword.isEmpty() && !folderID.isEmpty()) {
            // create document to insert into database
            Document folderDoc = new Document("folderId", folderID)
                    .append("folderPassword", folderPassword)
                    .append("folderName", folderName)
                    .append("IPAddress", currentIP);

            // insert foldername, folderid, folderpassword, and user ip into database
            collection.insertOne(folderDoc);

            // open send scene
            Parent root = FXMLLoader.load(getClass().getResource("/send-view.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
