package Controllers;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class CustomFileViewController extends FileView {
    private ImageIcon folderIcon = new ImageIcon("path/to/folder/icon.png");
    private ImageIcon fileIcon = new ImageIcon("path/to/file/icon.png");

    // Override the getName method to provide custom names for files and folders
    public String getName(File file) {
        String name = file.getName();
        if (name.equals("")) {
            name = file.getAbsolutePath();
        }
        return name;
    }

    // Override the getIcon method to provide custom icons for files and folders
    public Icon getIcon(File file) {
        if (file.isDirectory()) {
            return folderIcon;
        } else {
            return fileIcon;
        }
    }

    // Override the getDescription method to provide custom descriptions for files and folders
    public String getDescription(File file) {
        return file.getName();
    }
}
