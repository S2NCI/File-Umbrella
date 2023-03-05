/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.File;

/**
 * @authors Team 19
 */

public class Folder implements Serializable {
    private File directory;
    private int id;
    private String managementPassword;
    private ArrayList<String> members = new ArrayList<>();
    private ArrayList<FileData> savedFiles = new ArrayList<>();
    private boolean autoUpdate; //if true skip 
    private boolean autoShare;

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
        return directory.getName();
    }

    public Folder(File directory, int id, String managementPassword, boolean autoUpdate, boolean autoShare) {
        this.directory = directory;
        this.id = id;
        this.managementPassword = managementPassword;
        this.autoUpdate = autoUpdate;
        this.autoShare = autoShare;
        createFolder();
    }
    
    
    private void createFolder() {
        //method to create a directory folder to read files from
    
    }
    
    public void deleteFolder() {
        //method to optionally delete the directory when deleting the folder
        
    }
    
    public void checkIn() throws IOException {
        //method to refresh folder data in general
        ArrayList<FileData> send = checkForChanges();
        if(autoShare) { 
            sendNotif(send); 
        } else {
            //request permission
        }
        
    }
    
    public void recieveNotif(ArrayList<FileData> recieve) {
        if(autoUpdate) {
            //request files that are new from the recieve notification
            requestFiles(getNewFiles(recieve));
        } else {
            //request permission
        }
    }
    
    public void sendNotif(ArrayList<FileData> notify) {
        //method to send notice of file changes to network members
    }
    
    public void requestFiles(ArrayList<FileData> request) {
        //method to request files from a network member outlined in a recieved notification
    
    }
    
    public void sendFiles(ArrayList<FileData> send) {
        //method to send files to a network member
        
    }
    
    protected ArrayList<FileData> checkForChanges() throws IOException {
        //method to check for changes in the folder to send to other devices and to internally log
        File[] directoryFiles = directory.listFiles();
        ArrayList<FileData> distributeFiles = new ArrayList<>();
        
        LocalDateTime existingTime, updatedTime;
        
        for(File file : directoryFiles) {
            if(file.isDirectory() == false) {
                for(FileData local : savedFiles) {
                    if(file.getName().matches(local.getName())) {
                        //index previous time and update before comparing
                        existingTime = local.getLocalDate();
                        local.updateTime(directory);
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
            f.updateTime(directory);
            savedFiles.add(f);
            distributeFiles.add(f);
        }
        
        return distributeFiles;
    }
    
    protected ArrayList<FileData> getNewFiles(ArrayList<FileData> comparedFiles) {
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
}
