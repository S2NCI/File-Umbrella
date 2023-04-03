package Controllers;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;

import java.io.IOException;
import java.util.UUID;

public class JoinCreateFolderController {

    // JavaFX fields
    @FXML
    public Label btnCreateFolder;
    @FXML
    public TextField folderGen;
    @FXML
    public TextField folderPass;
    @FXML
    public TextField folderNameTF;


    // JavaFX methods to handle events
    @FXML
    private void handleAccountAuth(ActionEvent event) {

        String currentIP = null;

        // determining user's IP to compare with database
        try {
            currentIP = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // establish connection to MongoDB database
        String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = DBController.createConnection(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserConnection");
        MongoCollection<Document> collection = database.getCollection("IPAddress");

        // query database for user's IP
        Document query = new Document("IPAddress", currentIP);
        FindIterable<Document> result = collection.find(query);

        // if user's IP is found in database, open send scene
        if (result.first() != null) {
            // open send scene
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/send-view.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // if user's IP is not found in database, open dialog box
            Alert alert = new Alert(Alert.AlertType.WARNING, "You are not a member of any folders");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK){
                alert.close();
            }
        }
    }

    // open create folder scene when create folder button is clicked
    @FXML
    private void handleCreateFolder(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/createfolder-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // cancel folder creation
    @FXML
    private void cancelCreate(MouseEvent event){
        System.exit(0);
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
            currentIP = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // establish connection to MongoDB database
        String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = DBController.createConnection(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserConnection");
        MongoCollection<Document> collection = database.getCollection("IPAddress");

        // create document to insert into database
        Document folderDoc = new Document ("folderId", folderID)
                .append("folderPassword", folderPassword)
                .append("folderName", folderName)
                .append("IPAddress", currentIP);

        // insert foldername, folderid, folderpassword, and user ip into database
        collection.insertOne(folderDoc);
    }

}
