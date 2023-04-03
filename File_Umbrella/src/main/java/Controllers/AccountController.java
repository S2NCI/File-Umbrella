package Controllers;

import java.net.InetAddress;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;

import java.io.IOException;

public class AccountController {
    @FXML
    public Label btnCreateFolder;
    @FXML
    Label userIPLabel;


    @FXML
    private void handleAccountAuth(ActionEvent event) {

        String currentIP = null;

        try {
            currentIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = DBController.createConnection(connectionString);
        MongoDatabase database = mongoClient.getDatabase("UserConnection");
        MongoCollection<Document> collection = database.getCollection("IPAddress");

        Document query = new Document("IPAddress", currentIP);
        FindIterable<Document> result = collection.find(query);

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
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You are not a member of any folders");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK){
                alert.close();
            }
        }
    }

    @FXML
    private void handleCreateFolder(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/createfolder-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

}
