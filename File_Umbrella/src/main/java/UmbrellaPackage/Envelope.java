/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.util.ArrayList;

/**
 * @authors Team 19
 */
public class Envelope {
    private String id;
    private ArrayList<FileData> sentFiles = new ArrayList<>();
    private boolean request;

    public String getId() {
        return id;
    }

    public ArrayList<FileData> getSentFiles() {
        return sentFiles;
    }

    public void setSentFiles(ArrayList<FileData> sentFiles) {
        this.sentFiles = sentFiles;
    }

    public boolean isRequest() {
        return request;
    }

    public Envelope(String id, boolean isRequest, ArrayList<FileData> sentFiles) {
        this.id = id;
        this.request = isRequest;
        this.sentFiles = sentFiles;
    }
}
