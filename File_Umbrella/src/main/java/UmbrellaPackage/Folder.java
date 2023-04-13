/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String userHome = System.getProperty("user.home");
    private ArrayList<FileData> savedFiles = new ArrayList<>();
    private boolean autoUpdate; //if true skip 
    private boolean autoShare;

    public ArrayList<String> getMembers() {
        return members;
    }

    public void addMember(String newMember) {
        members.add(newMember);
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
        String documentsPath = userHome + "\\Documents\\File Umbrella\\" + folderName;
        File directory = new File(documentsPath);
    
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private void renameFolder(String oldName) {
        String oldPath = userHome + "\\Documents\\File Umbrella\\" + oldName;
        String newPath = userHome + "\\Documents\\File Umbrella\\" + folderName;

        File oldFolder = new File(oldPath);
        File newFolder = new File(newPath);

        if (oldFolder.exists()) {
            oldFolder.renameTo(newFolder);
        } else {
            //if no folder to rename then create said folder
            createFolder(folderName);
        }
    }

    public void deleteDirectory() {
        //method to optionally delete the directory when deleting the folder
        
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
        
        for(String IP : members) {
            //TODO attempt to send this envelope to each member ip through socket
            //FileDistributor.sendEnvelope(e);
        }
    }
    
    public void recieveFiles() {
        //method to save recieved files to the directory
        
    }
    
    public void sendFiles(ArrayList<FileData> recievedFiles, String destinationLocation) {
        //method to send files to a network member
        
    }

    //#region Syncing and Comparitors

    public void checkIn() {
        //method to refresh folder data in general
        
        //check for any file changes while application was closed
        ArrayList<FileData> send = checkForChanges();
        if(autoShare || true) {//request permission, true is a UI placeholder
            sendChanges(send); 
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
                            // TODO Auto-generated catch block
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
                // TODO Auto-generated catch block
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
