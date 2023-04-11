/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Objects;

/**
 * @authors Team 19
 * @partialsource https://heptadecane.medium.com/file-transfer-via-java-sockets-e8d4f30703a5
 */
public class FileReceiver {
    @FXML
    public Button sendSceneBtn;
    @FXML
    public Button viewIncomingBtn;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private static void sendFile(String path) throws Exception {
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        dataOutputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }


    // opens send scene
    public void handleSendScene(ActionEvent event) throws IOException {
        // open send scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/send-view.fxml")));
        Scene sendScene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(sendScene);
        window.show();
    }

    public void handleFileReceive(ActionEvent event) throws IOException {
        byte[] receiveBuffer = new byte[1024];
        InetAddress group = InetAddress.getByName("224.0.0.1");

        try (MulticastSocket multicastSocket = new MulticastSocket(new InetSocketAddress(group, 8888))) {
            multicastSocket.joinGroup(new InetSocketAddress(group, 8888), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            multicastSocket.receive(receivePacket);
            multicastSocket.leaveGroup(new InetSocketAddress(group, 8888), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));

            String message = new String(receivePacket.getData()).trim();
            InetAddress senderAddress = receivePacket.getAddress();
            int senderPort = receivePacket.getPort();

            try (Socket socket = new Socket(senderAddress, senderPort)) {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                List<File> fileList = (List<File>) objectInputStream.readObject();
                objectInputStream.close();

                for (File file : fileList) {
                    InputStream inputStream = socket.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
