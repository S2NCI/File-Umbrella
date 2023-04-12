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
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
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
        FileReceiver server = new FileReceiver();
        SocketChannel socketChannel = server.createServerSocketChannel();
        int bufferSize = 1024;
        Path filePath = Paths.get("C:\\Users\\Downloads\\test.txt");
        server.readFileFromSocketChannel(socketChannel, filePath, bufferSize);
    }

    private void readFileFromSocketChannel(SocketChannel socketChannel, Path filePath, int bufferSize) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE));
             WritableByteChannel bufferedChannel = Channels.newChannel(new BufferedOutputStream(Channels.newOutputStream(fileChannel), bufferSize))) {

            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                bufferedChannel.write(buffer);
                buffer.clear();
            }
        }
    }


    private SocketChannel createServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocket = null;
        SocketChannel client = null;
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(9000));
        client = serverSocket.accept();
        System.out.println("Connection established" + client.getRemoteAddress());
        return client;
    }

}
