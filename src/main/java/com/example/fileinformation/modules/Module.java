package com.example.fileinformation.modules;

import java.io.File;

public abstract class Module {
    public abstract boolean isMyExtension(File file);
    
    public abstract String functionalDescription();
    
    public abstract void doCommand(String command, File file);
    
    public String getExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }
}
