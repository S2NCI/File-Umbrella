package Controllers;

import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import UmbrellaPackage.Main;
import com.jcraft.jsch.*;
import com.mongodb.BasicDBObject;
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
import org.bson.Document;

import java.io.IOException;
import java.util.UUID;

public class JoinCreateFolderController {

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
    private String FTP_SERVER = "n6dpm7grhgzdg.westeurope.azurecontainer.io";
    private final int FTP_PORT = 22;
    private final String FTP_USERNAME = "sftp";
    private final String FTP_PASSWORD = "teamproject2023";
    private final String REMOTE_DIRECTORY = "/upload/";


    // JavaFX methods to handle events
    @FXML
    private void handleAccountAuth(ActionEvent event) throws JSchException, IOException {

        String folderId = folderIDTF.getText();

        // check if directory exists on server
        JSch jsch = new JSch();
        Session session = jsch.getSession(FTP_USERNAME, FTP_SERVER, FTP_PORT);
        session.setPassword(FTP_PASSWORD);
        System.out.println(folderId);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        SftpATTRS attrs = null;
        try {
            attrs = channelSftp.stat(REMOTE_DIRECTORY + "/" + folderId);
        } catch (Exception e) {
            // Handle the exception
        }
        if (attrs != null && attrs.isDir()) {
            System.out.println("Directory exists");
            // store the folder id in a file to be used in the next scene
            FileWriter fw = new FileWriter("folderID.txt");
            fw.write(folderId);
            fw.close();
            // open send-view
            Parent root = FXMLLoader.load(getClass().getResource("/send-view.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Directory does not exist");
        }
        channelSftp.disconnect();
        session.disconnect();
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
    private void handleFolderCreation(ActionEvent event) throws IOException, JSchException, SftpException {
        String folderID = folderGen.getText();
        String folderPassword = folderPass.getText();
        String folderName = folderNameTF.getText();

        // create folder in the SFPT server using the folder ID and Jsch library
        JSch jsch = new JSch();
        Session session = jsch.getSession(FTP_USERNAME, FTP_SERVER, FTP_PORT);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(FTP_PASSWORD);
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        // create folder in the SFPT server using the folder ID and Jsch library
        channel.mkdir(REMOTE_DIRECTORY + folderID);
        channel.disconnect();
        session.disconnect();

        // write folder ID to a file named folderID.txt to be used in the next scene
        FileWriter fw = new FileWriter("folderID.txt");
        fw.write(folderID);
        fw.close();

        // if successful, open send-view
        Parent root = FXMLLoader.load(getClass().getResource("/send-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
