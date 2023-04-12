/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import io.github.cdimascio.dotenv.Dotenv;

import com.jcraft.jsch.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * @authors Team 19
 * @partialsource https://heptadecane.medium.com/file-transfer-via-java-sockets-e8d4f30703a5
 */
public class FileReceiver implements Initializable {
    @FXML
    public Button sendSceneBtn;
    @FXML
    public Button viewIncomingBtn;
    @FXML
    public Button saveFilesToDeviceBtn;
    @FXML
    public Button refreshFiles;
    @FXML
    public FontAwesomeIconView downloadFolderIcon;
    private ObservableList<String> fileNames = FXCollections.observableArrayList();
    public TreeView listDownloads;
    private File selectedDirectory;
    private String FTP_SERVER = "n6dpm7grhgzdg.westeurope.azurecontainer.io";
    private final int FTP_PORT = 22;
    private final String FTP_USERNAME = "sftp";
    private final String FTP_PASSWORD = "teamproject2023";
    private final String REMOTE_DIRECTORY = "/upload/";

    // opens send scene
    public void handleSendScene(ActionEvent event) throws IOException {
        // open send scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/send-view.fxml")));
        Scene sendScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(sendScene);
        window.show();
    }

    public void handleFileReceive(ActionEvent event) throws JSchException, SftpException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(FTP_USERNAME, FTP_SERVER, FTP_PORT);
        session.setPassword(FTP_PASSWORD);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        Vector<ChannelSftp.LsEntry> entries = channel.ls(REMOTE_DIRECTORY);
        System.out.println("Files in remote directory: " + REMOTE_DIRECTORY);
        for (ChannelSftp.LsEntry entry : entries) {
            viewIncomingBtn.setVisible(false);
            downloadFolderIcon.setVisible(false);
            refreshFiles.setVisible(true);
            saveFilesToDeviceBtn.setVisible(true);
            // hide the . and .. directories
            if (entry.getFilename().equals(".") || entry.getFilename().equals("..")) {
                continue;
            }
            // sync the files from the remote directory
            fileNames.add(entry.getFilename());
            channel.get(REMOTE_DIRECTORY + entry.getFilename());
            System.out.println(entry.getFilename());
            // add the files to the tree view
            TreeItem<String> item = new TreeItem<>(entry.getFilename());
            listDownloads.getRoot().getChildren().add(item);
            // collapse the tree view
            listDownloads.getRoot().setExpanded(true);
        }

        channel.disconnect();
        session.disconnect();
    }


    public void saveToDevice(ActionEvent actionEvent) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(FTP_USERNAME, FTP_SERVER, FTP_PORT);
        session.setPassword(FTP_PASSWORD);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            for (String filename : fileNames) {
                File localFile = new File(selectedDirectory.getPath() + "/" + filename);
                try {
                    channel.get(REMOTE_DIRECTORY + filename, new FileOutputStream(localFile));
                } catch (SftpException | FileNotFoundException e) {
                    e.printStackTrace();
                }
                // javafx dialog box to show that the files have been saved
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("File Transfer");
                alert.setHeaderText("Transfer Successful");
                alert.setContentText("Files have been saved to your device");
                alert.showAndWait();
                // open the directory
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + localFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> root = new TreeItem<>("Received Files", new ImageView(new Image(getClass().getResourceAsStream("/Folder_Icon.png"))));
        listDownloads.setRoot(root);
    }

    public void syncFiles(ActionEvent actionEvent) throws JSchException, SftpException {
        // clear the tree view
        listDownloads.getRoot().getChildren().clear();
        // call the handleFileReceive method
        handleFileReceive(actionEvent);
    }
}
