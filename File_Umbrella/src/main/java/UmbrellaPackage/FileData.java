/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @authors Team 19
 */
public class FileData implements Serializable {
    private final String filename;
    private FileTime time;
    
    public FileData(String filename) {
        this.filename = filename;
    }
    
    protected void updateTime(File directory) throws IOException {
        //method to update the modified time from a file
        Path file = Paths.get(directory + filename);
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        time = attr.lastModifiedTime();

        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
    }
    
    protected FileTime getModDate() {
        return time;
    }
    
    protected LocalDateTime getLocalDate() {
        // Convert the FileTime to Localised time and return
        return LocalDateTime.ofInstant( time.toInstant(), ZoneId.systemDefault());
    }
    
    protected String getName() {
        return filename;
    }
    
}
