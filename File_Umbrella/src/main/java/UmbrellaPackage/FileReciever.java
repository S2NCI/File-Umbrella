/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @authors Team 19
 * @partialsource 
 * https://heptadecane.medium.com/file-transfer-via-java-sockets-e8d4f30703a5
 */
public class FileReciever {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    /*public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5000)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            sendFile("path/to/file1.pdf");
            sendFile("path/to/file2.pdf");
            
            dataInputStream.close();
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        // send file size
        dataOutputStream.writeLong(file.length());  
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }
}
