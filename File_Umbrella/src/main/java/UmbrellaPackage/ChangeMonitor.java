/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;

import java.nio.file.*;

/**
 * @authors Team 19
 */

public class ChangeMonitor {

    public static void monitorFolder() {
        try {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path folder = Paths.get(Controllers.SettingsController.defaultDirectoryPath + "\\File Umbrella");
        folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();
            Path filename = (Path)event.context();

            if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                System.out.println(filename + " deleted.");
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                System.out.println(filename + " modified.");
            }
            }
            key.reset();
        }

        } catch (Exception e) {
        System.out.println("An error occurred while monitoring the folder.");
        e.printStackTrace();
        }
    }
}