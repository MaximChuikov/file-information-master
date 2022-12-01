package com.example.fileinformation.modules;

import org.springframework.stereotype.Component;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Component
public class MusicModule extends Module {
    @Override
    public boolean isMyExtension(File file) {
        String extension = getExtension(file);
        return extension != null && extension.equals("mp3");
    }
    
    @Override
    public String functionalDescription() {
        return "name - Название трека\n" +
            "time - Длительность в секундах\n";
    }
    
    @Override
    public void doCommand(String command, File file) {
        switch (command.toLowerCase()) {
            case "name":
                printName(file);
                break;
            case "time":
                printTime(file);
                break;
        }
    }
    
    private void printName(File file) {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            if (fileFormat instanceof TAudioFileFormat) {
                System.out.println(fileFormat.properties().get("title"));
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void printTime(File file) {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            if (fileFormat instanceof TAudioFileFormat) {
                System.out.println((long) fileFormat.properties().get("duration") / (1000 * 1000));
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
