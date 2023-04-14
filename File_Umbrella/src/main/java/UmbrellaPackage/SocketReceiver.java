package UmbrellaPackage;

import java.io.*;
import java.net.*;
import java.io.IOException;

public class SocketReceiver {

    public static Envelope receiveEnvelope() throws IOException, ClassNotFoundException {
        
        ServerSocket serverSocket = new ServerSocket(5002);
        Socket socket = serverSocket.accept();
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Envelope e = (Envelope)inputStream.readObject();
        inputStream.close();
        socket.close();
        serverSocket.close();
        return e;
    }

    /*Thread receiverThread = new Thread(() -> {
        try {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    });*/
}


/*sockets as intended implementation
public class FileReciever {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println("listening to port:5000");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected.");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            receiveFile("NewFile1.pdf");
            receiveFile("NewFile2.pdf");

            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        
        long size = dataInputStream.readLong();     // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }
}
*/

