/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

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
// import arrays
import java.util.Arrays;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.IOUtils;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

import java.awt.*;
import java.io.*;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
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
    public TreeView<Path> listDownloads;
    private File selectedDirectory;
    private Tika tika = new Tika();
    private int fileCount = 0;
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
            // file count
            fileCount++;
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
            String filename = entry.getFilename();
            Path filepath = Paths.get(filename); // create a Path object from the filename
            TreeItem<Path> item = new TreeItem<>(filepath);
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
            }
            // Display alert with file count and option to open directory
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("File Download Complete");
            alert.setContentText(String.format("%d files have been saved to %s", fileNames.size(), selectedDirectory.getAbsolutePath()));
            ButtonType openDirectoryButton = new ButtonType("Open Directory", ButtonBar.ButtonData.LEFT);
            alert.getButtonTypes().setAll(openDirectoryButton, ButtonType.OK);
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == openDirectoryButton) {
                    try {
                        Desktop.getDesktop().open(selectedDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private TreeItem<Path> createTreeItem(Path path) {
        TreeItem<Path> item = new TreeItem<>(path);
        if (Files.isDirectory(path)) {
            try {
                Files.list(path)
                        .filter(Files::isReadable)
                        .forEach(childPath -> {
                            TreeItem<Path> childItem = createTreeItem(childPath);
                            item.getChildren().add(childItem);
                        });
            } catch (IOException e) {
                // ignore
            }
        }
        return item;
    }

    private boolean isPreviewable(String extension) {
        return Arrays.asList("pdf", "doc", "docx", "txt", "xml", "html", "xhtml", "ppt", "pptx", "xls", "xlsx", "csv", "tsv")
                .contains(extension.toLowerCase());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<Path> root = new TreeItem<>(Paths.get("Received Files"), new ImageView(new Image(getClass().getResourceAsStream("/Folder_Icon.png"))));
        listDownloads.setRoot(root);
        // create a web view to preview the files
    }


    public void syncFiles(ActionEvent actionEvent) throws JSchException, SftpException {
        // clear the tree view
        listDownloads.getRoot().getChildren().clear();
        // call the handleFileReceive method
        handleFileReceive(actionEvent);
    }


    @FXML
    public void handlePreview(MouseEvent mouseEvent) throws TikaException, IOException, JSchException, SftpException {
        // get the selected file
        TreeItem<Path> selectedItem = listDownloads.getSelectionModel().getSelectedItem();
        // check for double click and if the item is an SFTP file
        if (mouseEvent.getClickCount() == 2 && selectedItem != null) {
            // get the file name
            String filename = selectedItem.getValue().toString();
            // get the file extension
            String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);
            // check if file is previewable
            if (isPreviewable(fileExtension)) {
                // download the file from the SFTP server and create a temporary file
                JSch jsch = new JSch();
                Session session = jsch.getSession(FTP_USERNAME, FTP_SERVER, FTP_PORT);
                session.setPassword(FTP_PASSWORD);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();
                ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
                InputStream inputStream = channel.get(REMOTE_DIRECTORY + filename);
                // create a non temporary file
                Path tempFile = Files.createFile(Paths.get(filename));
                // write the file contents to the temporary file
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // close the channel and session
                channel.disconnect();
                session.disconnect();
                // get the file contents
                String fileContents = tika.parseToString(tempFile.toFile());
                // create a web view to display the file contents
                WebView webView = new WebView();
                webView.getEngine().loadContent(fileContents);
                // display the file contents in a web view
                Stage stage = new Stage();
                stage.setTitle(filename);
                stage.setScene(webView.getScene());
                stage.show();
                // delete the temporary file when the stage is closed
                stage.setOnCloseRequest(event -> {
                    try {
                        Files.delete(tempFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                // display an alert that the file type is not supported
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("File Type Not Supported");
                alert.setContentText("The file type is not supported for previewing.");
                alert.showAndWait();
            }
        }
    }


}
