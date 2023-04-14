/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @authors Team 19
 */

public class Folder implements Serializable {
    private String folderName;
    private String id;
    private String accessPassword;
    private LocalDateTime lastSync;
    private ArrayList<String> members;
    private ArrayList<FileData> savedFiles = new ArrayList<>();

    private static String folderPath = Controllers.SettingsController.defaultDirectoryPath + "\\";
    private boolean autoUpdate = Controllers.SettingsController.autoUpdate; //if true skip 
    //private boolean autoShare = Controllers.SettingsController.autoShare; //if true share changes on startup and when files requested

    public ArrayList<String> getMembers() {
        return members;
    }

    public String getAccessPassword() {
        return this.accessPassword;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        String oldName = this.folderName;
        this.folderName = folderName;
        renameFolder(oldName);
    }

    public String getID() {
        return this.id;
    }

    public LocalDateTime getLastSync() {
        return this.lastSync;
    }

    public void setLastSync(LocalDateTime lastSync) {
        this.lastSync = lastSync;
    } 
    
    public void resetLastSync() {
        this.lastSync = LocalDateTime.now();
    }

    public Folder(String folderName, String id, String accessPassword) {
        //"folder" opened on program startup
        this.folderName = folderName;
        this.id = id;
        this.accessPassword = accessPassword;
        this.lastSync = LocalDateTime.now();
        createFolder(folderName);
        //update contents
        checkIn();
    }
    
    //#region directory
    
    private void createFolder(String folderName) {
        //method to create a directory folder to store folder items in
        String documentsPath = folderPath + folderName + " - " + id;
        File directory = new File(documentsPath);
    
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private void renameFolder(String oldName) {
        //Folder names follow a "Name-idcode" format so each remains unique
        String oldPath = folderPath + oldName + " - " + id;
        String newPath = folderPath + folderName + " - " + id;

        File oldFolder = new File(oldPath);
        File newFolder = new File(newPath);

        if (oldFolder.exists()) {
            oldFolder.renameTo(newFolder);
        } else {
            //if no folder to rename then create said folder
            createFolder(folderName);
        }
    }

    public void deleteFolder() {
        //method to optionally delete the directory when leaving the folder
        String folderLocation = folderPath + folderName + " - " + id;
        FileUtils.deleteQuietly(new File(folderLocation));
    }

    //#endregion

    //#region sync process

    public void sendChanges(ArrayList<FileData> changedFiles) {
        //method to send notice of file changes to network members
        Envelope e = new Envelope(id, false, changedFiles);
        
        for(String IP : members) {
            //attempt to send this envelope to each member ip through socket/SFTP
            try {
                SocketSender.sendEnvelope(e, IP);
            } catch (IOException e1) {
                // TODO envelope send failure
                e1.printStackTrace();
            }
        }
    }
    
    public void requestFiles(ArrayList<FileData> recievedFiles, String sourceIP) {
        //method to request files from a specific network member
        if(autoUpdate || true) {//request permission, true is a UI placeholder
            //create envelope of files to be requested
            Envelope e = new Envelope(id, true, compareFiles(recievedFiles));
            
            //move over socket/SFTP
            try {
                SocketSender.sendEnvelope(e, sourceIP);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }
    }
    
    public void sendFiles(ArrayList<FileData> requestedFiles, String destinationIP) {
        //method to send files to a specific network member
        
        resetLastSync();
    }

    //#endregion

    //#region Syncing and Comparitors

    public void checkIn() {
        //method to refresh folder data in general
        
        //check for any file changes while application was closed
        ArrayList<FileData> send = checkForChanges();
        sendChanges(send); 
        updateMembers();
    }

    private void updateMembers() {
        //update list of members
        try {
            // establish connection to MongoDB database
            String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
            MongoClient mongoClient = Controllers.DBController.createConnection(connectionString);
            MongoDatabase database = mongoClient.getDatabase("UserConnection");
            MongoCollection<Document> folderCollection = database.getCollection("FolderCollection");
            MongoCollection<Document> addressCollection = database.getCollection("IPCollection");

            BasicDBObject query = new BasicDBObject();
            query.put("folderId", id);
            query.put("folderPassword", accessPassword);

            Document result = folderCollection.find(query).first();

            // Search for documents with the specified key value
            Document query2 = new Document("folderId", id);
            MongoCursor<Document> cursor = addressCollection.find(query2).iterator();

            //if folder is found use connection to get destination IPs using that shared identifier
            if (result != null) {
                members = new ArrayList<>();

                // Loop through the matching documents and add their "secondary" values to the ArrayList
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    String addressValue = doc.getString("memberAddress");
                    members.add(addressValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Controllers.DBController.closeConnection();
        }
    }

    private ArrayList<FileData> checkForChanges() {
        //method to check for changes in the folder to send to other devices and to internally log
        
        String path = folderPath + folderName + " - " + id;
        File directory = new File(path);
        File[] directoryFiles = directory.listFiles();
        ArrayList<FileData> distributeFiles = new ArrayList<>();
        
        LocalDateTime updatedTime;

        //check for deleted files
        for(FileData local : savedFiles) {
            Boolean contains = false; 
            //check each directory file for it it matches the saved file
            for(File file : directoryFiles) {
                if(file.getName().matches(local.getName())) {
                    contains = true;
                    break;
                }
            }
            if(!contains) {
                savedFiles.remove(local);
            }
        }
        
        for(File file : directoryFiles) {
            if(file.isDirectory() == false) {
                for(FileData local : savedFiles) {
                    if(file.getName().matches(local.getName())) {
                        updatedTime = local.getLocalDate();
                        //if the file update time is after previous sync 
                        if(updatedTime.isAfter(lastSync)) {
                            distributeFiles.add(local);
                        }
                        //once file is found in record can move on to next
                        break;
                    }
                }
            }
            //if the file doesn't exist in record create the record and distribute
            FileData f = new FileData(file.getName());
            try {
                f.updateTime(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
            savedFiles.add(f);
            distributeFiles.add(f);
        }
        
        return distributeFiles;
    }
    
    private ArrayList<FileData> compareFiles(ArrayList<FileData> comparedFiles) {
        //Method to compare the modification date of local files and those contained in the sync notification
        ArrayList<FileData> requestFiles = new ArrayList<>();
        
        //Compare all local files to each foreign file, any which are newer or don't exist will be requested for transfer
        for(FileData foreign : comparedFiles) {
            for(FileData local : savedFiles) {
                //check to make sure the same files are being compared
                if(local.getName().matches(foreign.getName())) {
                    //if the foreign modification time is larger add it to a list of files to request downloaded
                    if(local.getLocalDate().isBefore(foreign.getLocalDate())) {
                        requestFiles.add(foreign);
                    }
                    break;
                }
            }
            //if no matching files are found add to request
            requestFiles.add(foreign);
        }
        
        return requestFiles;
    }

    //#endregion
}
