package com.example.fileinformation.modules;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryModule extends Module {
    @Override
    public boolean isMyExtension(File file) {
        return file.isDirectory();
    }
    @Override
    public String functionalDescription() {
        return "files - Список файлов\n" + "size - Размер всех файлов\n" + "";
    }
    @Override
    public void doCommand(String command, File file) {
        switch (command.toLowerCase()) {
            case "files":
                printFiles(file);
                break;
            case "size":
                printSize(file);
                break;
        }
    }
    
    private void printFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
    
    private void printSize(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        double totalSize = 0;
        for (File curFile : files) {
            totalSize += ((double) curFile.length()) / 1024;
        }
        System.out.println(String.format("%.2f", totalSize) + " KB");
    }
}
