/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.List;

/**
 * @authors Team 19
 * @partialsource https://heptadecane.medium.com/file-transfer-via-java-sockets-e8d4f30703a5
 */
public class FileDistributor {

    // JavaFX fields
    @FXML
    public FontAwesomeIconView plusIcon;
    @FXML
    public FontAwesomeIconView sendIcon;
    @FXML
    public Button sendFilesBtn;
    @FXML
    public Button addBtn;
    @FXML
    public Button downloadSceneBtn;
    public ListView fileListView;
    @FXML
    public Button btnUpload;
    @FXML
    public FontAwesomeIconView uploadIcn;
    private ObservableList<File> fileList = FXCollections.observableArrayList();
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static final int BUFFER_SIZE = 4096;


    private static void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);

            long size = dataInputStream.readLong();     // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;      // read upto file size
            }
        } finally {
            fileOutputStream.close();
        }
    }


    public static void sendEnvelope(Envelope e) {

    }


    // JavaFX methods to handle events
    public void handleFileUpload(ActionEvent event) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        Platform.runLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select file(s)/folder(s) to upload");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            // just show the file name and not the full path and icon of the file type in the list

            // add a filter to only show files
            fileChooser.setAcceptAllFileFilterUsed(false);
            // add the file to the list
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // just display the file name
                String fileName = selectedFile.getName();
                fileList.add(new File(fileName));
                fileListView.setItems(fileList);
                // hide the upload button
                btnUpload.setVisible(false);
                btnUpload.setDisable(true);
                uploadIcn.setVisible(false);
                addBtn.setVisible(true);
                addBtn.setDisable(false);
                sendIcon.setVisible(true);
                sendFilesBtn.setVisible(true);
            }
        });
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

    //

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

        if (!droppedFiles.isEmpty()) {
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

        fileListView.setOnDragDropped(event -> {
            Dragboard drag = event.getDragboard();
            boolean success = false;
            if (drag.hasFiles()) {
                for (File file : drag.getFiles()) {
                    if (!fileList.contains(file)) {
                        fileListView.getItems().add(file);
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });


    }

    public void sendFile(MouseEvent event) throws IOException {
        byte[] multicastMessage = "DISCOVER_SERVER_REQUEST".getBytes();
        MulticastSocket multiSocket = new MulticastSocket();

        InetAddress group = InetAddress.getByName("224.0.0.1");

        multiSocket.joinGroup(group);

        DatagramPacket packet = new DatagramPacket(multicastMessage, multicastMessage.length, group, 8888);

        multiSocket.send(packet);
        multiSocket.leaveGroup(group);
        multiSocket.close();

        // send the file list and file data
        try (Socket socket = new Socket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            int port = 8888;
            socket.connect(new InetSocketAddress(host, port), 5000);

            // send file list
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(fileList);
            objectOutputStream.flush();

            // send file data
            for (File file : fileList) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    socket.getOutputStream().write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
            }

            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleDownloadScene(ActionEvent event) throws IOException {
        // open download scene
        Parent root = FXMLLoader.load(getClass().getResource("/download-view.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleFileUploadPlus(ActionEvent event) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        try {
            handleFileUpload(event);
        } catch (IllegalArgumentException e) {
            System.out.println("Method redirected to handleFileUpload");
        }
    }
}
