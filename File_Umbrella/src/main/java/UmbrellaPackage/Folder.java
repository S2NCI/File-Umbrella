/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.net.InetAddress;
import java.net.UnknownHostException;

import UmbrellaPackage.Main;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.UUID;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.attribute.FileTime;
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
    private FileTime lastSync;
    private ArrayList<String> members;
    private ArrayList<FileData> savedFiles = new ArrayList<>();

    private static String folderPath = Controllers.SettingsController.defaultDirectoryPath + "\\";
    private boolean autoUpdate = Controllers.SettingsController.autoUpdate; //if true skip 
    private boolean autoShare = Controllers.SettingsController.autoShare; //if true share changes on startup and when files requested

    public ArrayList<String> getMembers() {
        return members;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public boolean isAutoShare() {
        return autoShare;
    }

    public void setAutoShare(boolean autoShare) {
        this.autoShare = autoShare;
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
        return id;
    }

    public Folder(String folderName, String id, String accessPassword, boolean autoUpdate, boolean autoShare) {
        //"folder" opened on program startup
        this.folderName = folderName;
        this.id = id;
        this.accessPassword = accessPassword;
        this.autoUpdate = autoUpdate;
        this.autoShare = autoShare;
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

    
    
    public void requestFiles(ArrayList<FileData> recievedFiles, String sourceLocation) {
        //method to request files from a specific network member
        if(autoUpdate || true) {//request permission, true is a UI placeholder
            //create envelope of files to be requested
            Envelope e = new Envelope(id, true, compareFiles(recievedFiles));
            
            //TODO: move over socket
        }
    }
    
    public void sendChanges(ArrayList<FileData> changedFiles) {
        //method to send notice of file changes to network members
        Envelope e = new Envelope(id, false, changedFiles);
        s
        for(String IP : members) {
            //TODO attempt to send this envelope to each member ip through socket
            FileDistributor.sendEnvelope(e, IP);
        }
    }
    
    public void sendFiles() {
        //method to send files to a specific network member
        
    }
    
    public void recieveFiles(ArrayList<FileData> recievedFiles, String destinationLocation) {
        //method to compare recieved changes with local files
        
    }

    //#region Syncing and Comparitors

    public void checkIn() {
        //method to refresh folder data in general
        
        //check for any file changes while application was closed
        ArrayList<FileData> send = checkForChanges();
        if(autoShare || true) {//request permission, true is a UI placeholder
            sendChanges(send); 
        } 

        //update list of members
        try {
            // establish connection to MongoDB database
            String connectionString = "mongodb+srv://admin:dbpass@cluster0.jmttrjk.mongodb.net/?retryWrites=true&w=majority";
            MongoClient mongoClient = Controllers.DBController.createConnection(connectionString);
            MongoDatabase database = mongoClient.getDatabase("UserConnection");
            MongoCollection<Document> folderCollection = database.getCollection("FolderCollection");

            BasicDBObject query = new BasicDBObject();
            query.put("folderId", id);
            query.put("folderPassword", accessPassword);

            Document result = folderCollection.find(query).first();

            // when folder is found use connection to get IPs with the same id
            if (result != null) {
                BasicDBObject query2 = new BasicDBObject();
                query2.put
                .put("folderId", id);
                query2.put("folderPassword", accessPassword);

                Document result = folderCollection.find(query).first();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Controllers.DBController.closeConnection();
        }
    }

    private ArrayList<FileData> checkForChanges() {
        //method to check for changes in the folder to send to other devices and to internally log
        
        String path = userHome + "\\Documents\\FileUmbrella\\" + folderName;
        File directory = new File(path);
        File[] directoryFiles = directory.listFiles();
        ArrayList<FileData> distributeFiles = new ArrayList<>();
        
        LocalDateTime existingTime, updatedTime;
        
        for(File file : directoryFiles) {
            if(file.isDirectory() == false) {
                for(FileData local : savedFiles) {
                    if(file.getName().matches(local.getName())) {
                        //index previous time and update before comparing
                        existingTime = local.getLocalDate();
                        try {
                            local.updateTime(directory);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updatedTime = local.getLocalDate();

                        //if the updated time is after 
                        if(updatedTime.isAfter(existingTime)) {
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
