package UmbrellaPackage;

import javafx.scene.control.TreeItem;

import java.io.File;

public class FileTreeItem extends TreeItem<File> {

    public FileTreeItem(File file) {
        super(file);
    }

    @Override
    public boolean isLeaf() {
        return getValue().isFile();
    }
}

